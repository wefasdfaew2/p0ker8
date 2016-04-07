package sadaguro.poker8.model;

import sadaguro.poker8.util.BetCommand;
import sadaguro.poker8.util.PlayerInfo;

public class PlayerEntity extends PlayerInfo {
	private int handValue = 0;
	private BetCommand betCommand;
	private boolean showCards;

	public PlayerEntity() {
	}

	public boolean showCards() {
		return showCards;
	}

	public void showCards(boolean showCards) {
		this.showCards = showCards;
	}

	public BetCommand getBetCommand() {
		return betCommand;
	}

	public void setBetCommand(BetCommand betCommand) {
		this.betCommand = betCommand;
	}

	public int getHandValue() {
		return handValue;
	}

	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}
}
