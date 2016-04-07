package sadaguro.poker8.state;

import sadaguro.poker8.interfacesImpl.IState;
import sadaguro.poker8.model.ModelContext;
import sadaguro.poker8.util.ModelUtil;
import sadaguro.poker8.util.TexasHoldEmUtil.GameState;
import sadaguro.poker8.util.TexasHoldEmUtil.PlayerState;

public class CheckState implements IState<ModelContext> {
	public static final String NAME = "Next";
	private static final GameState[] GAME_STATE = GameState.values();
	private static final int[] OBATIN_CARDS = { 3, 1, 1, 0, 0 };

	public String getName() {
		return NAME;
	}

	private int indexByGameState(GameState gameState) {
		int i = 0;
		while (i < GAME_STATE.length && GAME_STATE[i] != gameState) {
			i++;
		}
		return i;
	}

	public boolean execute(ModelContext model) {
		int indexGameState = indexByGameState(model.getGameState());
		if (OBATIN_CARDS[indexGameState] > 0) {
			model.addCommunityCards(OBATIN_CARDS[indexGameState]);
		}
		model.setGameState(GAME_STATE[indexGameState + 1]);
		model.setLastActivePlayers(model.getActivePlayers());
		model.setBets(0);
		model.getPlayers().stream().filter(p -> p.isActive()).forEach(p -> p.setState(PlayerState.READY));
		model.setPlayerTurn(ModelUtil.nextPlayer(model, model.getDealer()));
		model.setLastBetCommand(null);
		model.setLastPlayerBet(null);
		return true;
	}

}
