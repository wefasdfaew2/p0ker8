package sadaguro.poker8;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sadaguro.poker8.elements.Card;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    
	private static void insert(Set<Card> cards, Card card){
		
		if(!cards.contains(card)){
			System.out.println("insertamos la carta : {}"+ card);
			cards.add(card);
		}else{
			System.out.println("La carta: {} ya estaba en el conjunto "+ card);
		}
	}
	
	public static void main( String[] args )
    {
		Set<Card> cards =  new HashSet<Card>();
		
		Card[] cards2insert = {
				new Card(Card.Suit.CLUB, Card.Rank.ACE),
				new Card(Card.Suit.CLUB, Card.Rank.TWO),
				new Card(Card.Suit.CLUB, Card.Rank.TRHEE),
				new Card(Card.Suit.CLUB, Card.Rank.ACE)
		};
		
		for (Card card : cards2insert) {
			insert(cards, card);
		}
        
    }
}
