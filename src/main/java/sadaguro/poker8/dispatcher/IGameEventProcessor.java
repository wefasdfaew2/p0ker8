package sadaguro.poker8.dispatcher;

@FunctionalInterface
public interface IGameEventProcessor<T> {
	public void process(T target, GameEvent event);
}