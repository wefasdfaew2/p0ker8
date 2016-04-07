package sadaguro.poker8.interfaces;

import sadaguro.poker8.util.GameException;
import sadaguro.poker8.util.Settings;

public interface IGameController {
	public void setSettings(Settings settings);

	public boolean addStrategy(IStrategy strategy);

	public void start() throws GameException;

	public void waitFinish();
}
