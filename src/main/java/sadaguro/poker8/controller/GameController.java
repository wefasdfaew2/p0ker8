package sadaguro.poker8.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sadaguro.poker8.dispatcher.GameEvent;
import sadaguro.poker8.dispatcher.GameEventDispatcher;
import sadaguro.poker8.dispatcher.IGameEventDispatcher;
import sadaguro.poker8.dispatcher.IGameEventProcessor;
import sadaguro.poker8.elements.Card;
import sadaguro.poker8.interfaces.IGameController;
import sadaguro.poker8.interfaces.IStrategy;
import sadaguro.poker8.timer.GameTimer;
import sadaguro.poker8.timer.IGameTimer;
import sadaguro.poker8.util.BetCommand;
import sadaguro.poker8.util.GameException;
import sadaguro.poker8.util.GameInfo;
import sadaguro.poker8.util.PlayerInfo;
import sadaguro.poker8.util.Settings;

public class GameController implements IGameController, Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
	private static final int DISPATCHER_THREADS = 1;
	private static final int EXTRA_THREADS = 2;
	public static final String SYSTEM_CONTROLLER = "system";
	public static final String INIT_HAND_EVENT_TYPE = "initHand";
	public static final String BET_COMMAND_EVENT_TYPE = "betCommand";
	public static final String END_GAME_PLAYER_EVENT_TYPE = "endGame";
	public static final String END_HAND_PLAYER_EVENT_TYPE = "endHand";
	public static final String CHECK_PLAYER_EVENT_TYPE = "check";
	public static final String GET_COMMAND_PLAYER_EVENT_TYPE = "getCommand";
	public static final String EXIT_CONNECTOR_EVENT_TYPE = "exit";
	public static final String ADD_PLAYER_CONNECTOR_EVENT_TYPE = "addPlayer";
	public static final String TIMEOUT_CONNECTOR_EVENT_TYPE = "timeOutCommand";
	public static final String CREATE_GAME_CONNECTOR_EVENT_TYPE = "createGame";
	private final Map<String, IGameEventDispatcher> players = new HashMap<>();
	private final List<String> playersByName = new ArrayList<>();
	private final Map<String, IGameEventProcessor<IStrategy>> playerProcessors;
	private final GameEventDispatcher<StateMachineConnector> connectorDispatcher;
	private final StateMachineConnector stateMachineConnector;
	private final IGameTimer timer;
	private Settings settings;
	private ExecutorService executors;
	private List<ExecutorService> subExecutors = new ArrayList<>();
	private boolean finish;

	public GameController() {
		timer = new GameTimer(SYSTEM_CONTROLLER, buildExecutor(DISPATCHER_THREADS));
		stateMachineConnector = new StateMachineConnector(timer, players);
		connectorDispatcher = new GameEventDispatcher<>(stateMachineConnector, buildConnectorProcessors(),
				buildExecutor(1));
		stateMachineConnector.setSystem(connectorDispatcher);
		timer.setDispatcher(connectorDispatcher);
		playerProcessors = buildPlayerProcessors();
	}

	private ExecutorService buildExecutor(int threads) {
		ExecutorService result = Executors.newFixedThreadPool(threads);
		subExecutors.add(result);
		return result;
	}

	@Override
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	private static Map<String, IGameEventProcessor<StateMachineConnector>> buildConnectorProcessors() {
		Map<String, IGameEventProcessor<StateMachineConnector>> cpm = new HashMap<>();
		cpm.put(CREATE_GAME_CONNECTOR_EVENT_TYPE, (c, e) -> c.createGame((Settings) e.getPayload()));
		cpm.put(ADD_PLAYER_CONNECTOR_EVENT_TYPE, (c, e) -> c.addPlayer(e.getSource()));
		cpm.put(INIT_HAND_EVENT_TYPE, (c, e) -> c.startGame());
		cpm.put(BET_COMMAND_EVENT_TYPE, (c, e) -> c.betCommand(e.getSource(), (BetCommand) e.getPayload()));
		cpm.put(TIMEOUT_CONNECTOR_EVENT_TYPE, (c, e) -> c.timeOutCommand((Long) e.getPayload()));
		return cpm;
	}

	private Map<String, IGameEventProcessor<IStrategy>> buildPlayerProcessors() {
		Map<String, IGameEventProcessor<IStrategy>> ppm = new HashMap<>();
		IGameEventProcessor<IStrategy> defaultProcessor = (s, e) -> s.updateState((GameInfo) e.getPayload());
		ppm.put(INIT_HAND_EVENT_TYPE, defaultProcessor);
		ppm.put(END_GAME_PLAYER_EVENT_TYPE, defaultProcessor);
		ppm.put(BET_COMMAND_EVENT_TYPE, (s, e) -> s.onPlayerCommand(e.getSource(), (BetCommand) e.getPayload()));
		ppm.put(CHECK_PLAYER_EVENT_TYPE, (s, e) -> s.check((List<Card>) e.getPayload()));
		ppm.put(GET_COMMAND_PLAYER_EVENT_TYPE, (s, e) -> {
			GameInfo<PlayerInfo> gi = (GameInfo<PlayerInfo>) e.getPayload();
			String playerTurn = gi.getPlayers().get(gi.getPlayerTurn()).getName();
			BetCommand cmd = s.getCommand(gi);
			connectorDispatcher.dispatch(new GameEvent(BET_COMMAND_EVENT_TYPE, playerTurn, cmd));
		});
		return ppm;
	}

	public synchronized boolean addStrategy(IStrategy strategy) {
		boolean result = false;
		String name = strategy.getName();
		if (!players.containsKey(name) && !SYSTEM_CONTROLLER.equals(name)) {
			players.put(name, new GameEventDispatcher<>(strategy, playerProcessors, buildExecutor(DISPATCHER_THREADS)));
			playersByName.add(name);
			result = true;
		}
		return result;
	}

	public synchronized void waitFinish() {
		if (!finish) {
			try {
				wait();
			} catch (InterruptedException ex) {
				LOGGER.error("Esperando el...", ex);
			}
		}
	}

	private void check(boolean essentialCondition, String exceptionMessage) throws GameException {
		if (!essentialCondition) {
			throw new GameException(exceptionMessage);
		}
	}

	@Override
	public synchronized void start() throws GameException {
		LOGGER.debug("start");
		check(settings != null, "No se ha establecido una configuración.");
		check(players.size() > 1, "No se han agregado un numero suficiente de j...");
		check(players.size() <= settings.getMaxPlayers(), "El número de jugador...");
		check(settings.getMaxErrors() > 0, "El número de máximo de errores debe...");
		check(settings.getMaxRounds() > 0, "El número de máximo de rondas debe ...");
		check(settings.getRounds4IncrementBlind() > 1, "El número de rondas hast...");
		check(settings.getTime() > 0, "El tiempo másimo por jugador debe ser ma...");
		check(settings.getPlayerChip() > 0, "El número de fichas inicial por ju...");
		check(settings.getSmallBind() > 0, "La apuesta de la ciega pequeña debe...");
		executors = Executors.newFixedThreadPool(players.size() + EXTRA_THREADS);
		players.values().stream().forEach(executors::execute);
		stateMachineConnector.createGame(settings);
		timer.setTime(settings.getTime());
		playersByName.stream().forEach(stateMachineConnector::addPlayer);
		executors.execute(timer);
		finish = false;
		new Thread(this).start();
	}

	@Override
	public synchronized void run() {
		LOGGER.debug("run");
		// Inicio de la ejecución del juego.
		connectorDispatcher.dispatch(new GameEvent(INIT_HAND_EVENT_TYPE, SYSTEM_CONTROLLER));
		connectorDispatcher.run();
		// Fin de la ejecución del juego.
		timer.exit();
		executors.shutdown();
		players.values().stream().forEach(IGameEventDispatcher::exit);
		subExecutors.stream().forEach(ExecutorService::shutdown);
		try {
			executors.awaitTermination(0, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			LOGGER.error("Error intentando eliminar cerrar todos los hilos", ex);
		}
		finish = true;
		notifyAll();
	}

}
