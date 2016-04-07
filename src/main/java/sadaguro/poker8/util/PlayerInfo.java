package sadaguro.poker8.util;

import sadaguro.poker8.elements.Card;
import sadaguro.poker8.util.TexasHoldEmUtil.PlayerState;

public class PlayerInfo {

	private String name;
	private long chips;
	private long bet;
	private Card[] cards = new Card[TexasHoldEmUtil.PLAYER_CARDS];
	private PlayerState state;
	private int errors;

	public PlayerInfo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getChips() {
		return chips;
	}

	public boolean isActive() {
		return state.isActive();
	}

	public void setChips(long chips) {
		this.chips = chips;
	}

	public void addChips(long chips) {
		this.chips += chips;
	}

	public long getBet() {
		return bet;
	}

	public void setBet(long bet) {
		this.bet = bet;
	}

	public Card[] getCards() {
		return new Card[] { cards[0], cards[1] };
	}

	public void setCards(Card[] cards) {
		this.cards[0] = cards[0];
		this.cards[1] = cards[1];
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public Card getCard(int index) {
		return cards[index];
	}

	public void setCards(Card card0, Card card1) {
		this.cards[0] = card0;
		this.cards[1] = card1;
	}
}
