package sadaguro.poker8.elements;

import org.junit.Test;

import junit.framework.*;

public class CardTest {
	
	@Test
	public void testConstructor(){
		System.out.println("Card()");
		Card.Suit expSuit = Card.Suit.CLUB;
		Card.Rank expRank = Card.Rank.TWO;
		Card instance = new Card(expSuit, expRank);
		Card.Suit suitResult = instance.getSuit();
		
		Card.Rank rankResult = instance.getRank();
		
		
	}

}
