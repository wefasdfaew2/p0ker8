package sadaguro.poker8.interfacesImpl;

import java.util.List;

import sadaguro.poker8.elements.Card;
import sadaguro.poker8.interfaces.IHandEvaluator;
import sadaguro.poker8.util.TexasHoldEmUtil;

public class Hand7Evaluator {

	public static final int TOTAL_CARDS = TexasHoldEmUtil.PLAYER_CARDS + TexasHoldEmUtil.COMMUNITY_CARDS;
	private final int[] combinatorialBuffer = new int[TexasHoldEmUtil.COMMUNITY_CARDS];
	private final Combination combinatorial = new Combination(TexasHoldEmUtil.COMMUNITY_CARDS, TOTAL_CARDS);
	private final IHandEvaluator evaluator;
	private final Card[] evalBuffer = new Card[TexasHoldEmUtil.COMMUNITY_CARDS];
	private final Card[] cards = new Card[TOTAL_CARDS];
	private int communityCardsValue = 0;

	public Hand7Evaluator(IHandEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public void setCommunityCards(List<Card> cc) {
		int i = 0;
		for (Card card : cc) {
			evalBuffer[i] = card;
			cards[i++] = card;
		}
		communityCardsValue = evaluator.eval(evalBuffer);
	}

	public int eval(Card c0, Card c1) {
		cards[TexasHoldEmUtil.COMMUNITY_CARDS] = c0;
		cards[TexasHoldEmUtil.COMMUNITY_CARDS + 1] = c1;
		return evalCards();
	}

	static Card[] copy(Card[] src, Card[] target, int[] positions) {
		int i = 0;
		for (int p : positions) {
			target[i++] = src[p];
		}
		return target;
	}

	private int evalCards() {
		combinatorial.clear();
		combinatorial.next(combinatorialBuffer);
		int result = communityCardsValue;
		while (combinatorial.hasNext()) {
			result = Math.max(result, evaluator.eval(copy(cards, evalBuffer, combinatorial.next(combinatorialBuffer))));
		}
		return result;
	}
}
