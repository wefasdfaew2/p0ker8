package sadaguro.poker8.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import sadaguro.poker8.model.ModelContext;
import sadaguro.poker8.model.PlayerEntity;
import sadaguro.poker8.util.GameInfo;
import sadaguro.poker8.util.PlayerInfo;
import sadaguro.poker8.util.Settings;

public final class PlayerAdapter {
	private PlayerAdapter() {
	}

	public static List<PlayerInfo> toPlayerInfo(Collection<PlayerEntity> players, String name) {
		List<PlayerInfo> result = Collections.emptyList();
		if (players != null) {
			result = new ArrayList<>(players.size());
			for (PlayerEntity pe : players) {
				result.add(copy(pe, pe.showCards() || pe.getName().equals(name)));
			}
		}
		return result;
	}

	public static GameInfo<PlayerInfo> toTableState(ModelContext model, String name) {
		GameInfo<PlayerInfo> result = new GameInfo<>();
		result.setCommunityCards(model.getCommunityCards());
		result.setDealer(model.getDealer());
		result.setGameState(model.getGameState());
		result.setPlayerTurn(model.getPlayerTurn());
		result.setRound(model.getRound());
		if (model.getSettings() != null) {
			result.setSettings(new Settings(model.getSettings()));
		}
		result.setPlayers(toPlayerInfo(model.getPlayers(), name));
		return result;
	}

	public static PlayerInfo copy(PlayerEntity p, boolean copyCards) {
		PlayerInfo result = new PlayerInfo();
		result.setName(p.getName());
		result.setChips(p.getChips());
		result.setBet(p.getBet());
		if (copyCards) {
			result.setCards(p.getCard(0), p.getCard(1));
		}
		result.setState(p.getState());
		result.setErrors(p.getErrors());
		return result;
	}
}
