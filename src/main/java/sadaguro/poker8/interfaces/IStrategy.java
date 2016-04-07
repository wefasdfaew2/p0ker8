package sadaguro.poker8.interfaces;

import java.util.List;

import sadaguro.poker8.elements.Card;
import sadaguro.poker8.util.BetCommand;
import sadaguro.poker8.util.GameInfo;
import sadaguro.poker8.util.PlayerInfo;

public interface IStrategy {
	
	public String getName();

	public BetCommand getCommand(GameInfo<PlayerInfo> state);

	public default void updateState(GameInfo<PlayerInfo> state) {
	}

	public default void check(List<Card> communityCards) {
	}

	public default void onPlayerCommand(String player, BetCommand betCommand) {
	}
}
