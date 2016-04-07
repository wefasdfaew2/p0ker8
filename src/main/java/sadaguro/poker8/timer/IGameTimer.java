package sadaguro.poker8.timer;

import sadaguro.poker8.dispatcher.IGameEventDispatcher;

public interface IGameTimer extends Runnable {
	public void exit();

	public long getTime();

	public void resetTimer(Long timeroutId);

	public void setTime(long time);

	public IGameEventDispatcher getDispatcher();

	public void setDispatcher(IGameEventDispatcher dispatcher);
}
