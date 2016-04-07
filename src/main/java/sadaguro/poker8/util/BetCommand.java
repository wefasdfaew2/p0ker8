package sadaguro.poker8.util;

import sadaguro.poker8.util.TexasHoldEmUtil.BetCommandType;

public class BetCommand {
	private final BetCommandType type;
	private long chips;

	public BetCommand(BetCommandType type, long chips) {
		ExceptionUtil.checkNullArgument(type, "type");
		ExceptionUtil.checkMinValueArgument(chips, 0L, "chips");
		this.type = type;
		this.chips = chips;
	}

	public BetCommand(BetCommandType type) {
		this(type, 0);
	}

	public BetCommandType getType() {
		return type;
	}

	public long getChips() {
		return chips;
	}

	public void setChips(long chips) {
		this.chips = chips;
	}
}
