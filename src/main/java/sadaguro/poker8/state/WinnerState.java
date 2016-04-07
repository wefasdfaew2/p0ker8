package sadaguro.poker8.state;

import java.util.List;

import sadaguro.poker8.interfacesImpl.IState;
import sadaguro.poker8.model.ModelContext;
import sadaguro.poker8.model.PlayerEntity;
import sadaguro.poker8.util.TexasHoldEmUtil.PlayerState;

public class WinnerState implements IState<ModelContext> {
	public static final String NAME = "Winner";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean execute(ModelContext model) {
		List<PlayerEntity> players = model.getPlayers();
		players.stream().filter(p -> p.isActive() || p.getState() == PlayerState.ALL_IN).findFirst().get()
				.addChips(players.stream().mapToLong(p -> p.getBet()).sum());
		return true;
	}

}
