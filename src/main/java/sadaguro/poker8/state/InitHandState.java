package sadaguro.poker8.state;

import java.util.List;

import sadaguro.poker8.elements.Deck;
import sadaguro.poker8.interfacesImpl.IState;
import sadaguro.poker8.model.ModelContext;
import sadaguro.poker8.model.PlayerEntity;
import sadaguro.poker8.util.ModelUtil;
import sadaguro.poker8.util.Settings;
import sadaguro.poker8.util.TexasHoldEmUtil;
import sadaguro.poker8.util.TexasHoldEmUtil.BetCommandType;
import sadaguro.poker8.util.TexasHoldEmUtil.GameState;
import sadaguro.poker8.util.TexasHoldEmUtil.PlayerState;

public class InitHandState implements IState<ModelContext> {
	public static final String NAME = "InitHand";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean execute(ModelContext model) {
		Deck deck = model.getDeck();
		deck.shuffle();
		Settings settings = model.getSettings();
		model.setGameState(GameState.PRE_FLOP);
		model.clearCommunityCard();
		model.setRound(model.getRound() + 1);
		if (model.getRound() % settings.getRounds4IncrementBlind() == 0) {
			settings.setSmallBind(2 * settings.getSmallBind());
		}
		model.setPlayersAllIn(0);
		model.setHighBet(0L);
		List<PlayerEntity> players = model.getPlayers();
		for (PlayerEntity p : players) {
			p.setState(PlayerState.READY);
			p.setHandValue(0);
			p.setBet(0);
			p.showCards(false);
			p.setCards(deck.obtainCard(), deck.obtainCard());
		}
		int numPlayers = model.getNumPlayers();
		model.setActivePlayers(numPlayers);
		int dealerIndex = (model.getDealer() + 1) % numPlayers;
		model.setDealer(dealerIndex);
		model.setPlayerTurn((dealerIndex + 1) % numPlayers);
		if (numPlayers > TexasHoldEmUtil.MIN_PLAYERS) {
			compulsoryBet(model, settings.getSmallBind());
		}
		compulsoryBet(model, settings.getBigBind());
		return true;
	}

	private void compulsoryBet(ModelContext model, long chips) {
		int turn = model.getPlayerTurn();
		PlayerEntity player = model.getPlayer(turn);
		if (player.getChips() <= chips) {
			player.setState(PlayerState.ALL_IN);
			ModelUtil.playerBet(model, player, BetCommandType.ALL_IN, player.getChips());
		} else {
			ModelUtil.playerBet(player, chips);
		}
		model.setHighBet(chips);
		model.setPlayerTurn((turn + 1) % model.getNumPlayers());
	}
}
