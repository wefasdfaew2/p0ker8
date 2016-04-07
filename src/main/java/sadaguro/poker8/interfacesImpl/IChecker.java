package sadaguro.poker8.interfacesImpl;

@FunctionalInterface
public interface IChecker<T> {
	public boolean check(T context);
}
