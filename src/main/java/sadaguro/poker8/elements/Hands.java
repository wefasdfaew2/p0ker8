package sadaguro.poker8.elements;

public final class Hands {
	
	public static final int CARDS = 5;
	
	public enum Type{
		HIGH_CARD,
		ONE_PAIR,
		TWO_PAIR,
		THREE_OF_A_KIND,
		STRAINGHT,
		FLUSH,
		FULL_HOUSE,
		FOUR_OF_A_KIND,
		STRAINGHT_FLUSH
	}
	
	private Hands(){
		
	}
}
