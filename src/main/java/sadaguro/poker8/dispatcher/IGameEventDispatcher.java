package sadaguro.poker8.dispatcher;

public interface IGameEventDispatcher extends Runnable {
	public void dispatch(GameEvent event);

	public void exit();

}
