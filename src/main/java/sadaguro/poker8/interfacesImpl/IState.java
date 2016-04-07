package sadaguro.poker8.interfacesImpl;

public interface IState<T> {
	public String getName();
	public boolean execute(T context);
}
