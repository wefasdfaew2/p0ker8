package sadaguro.poker8.state;

import java.util.ArrayList;
import java.util.List;

import sadaguro.poker8.interfacesImpl.IState;
import sadaguro.poker8.model.ModelContext;
import sadaguro.poker8.model.PlayerEntity;

public class EndState implements IState<ModelContext> {
	public static final String NAME = "EndHand";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean execute(ModelContext model) {
		PlayerEntity dealerPlayer = model.getPlayer(model.getDealer());
		List<PlayerEntity> players = model.getPlayers();
		List<PlayerEntity> nextPlayers = new ArrayList<>(players.size());
		int i = 0;
		int dealerIndex = 0;
		for (PlayerEntity p : players) {
			if (p.getChips() > 0) {
				nextPlayers.add(p);
				i++;
			}
			if (dealerPlayer == p) {
				dealerIndex = i - 1;
			}
		}
		model.setDealer(dealerIndex);
		model.setPlayers(nextPlayers);
		return true;
	}

}
