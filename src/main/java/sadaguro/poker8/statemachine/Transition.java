package sadaguro.poker8.statemachine;

import sadaguro.poker8.interfacesImpl.IChecker;
import sadaguro.poker8.interfacesImpl.IState;

public class Transition<T> {
	private final IState<T> origin;
	private final IState<T> target;
	private final IChecker<T> checker;

	public Transition(IState<T> origin, IState<T> target, IChecker<T> checker) {
		this.origin = origin;
		this.target = target;
		this.checker = checker;
	}

	public IState<T> getOrigin() {
		return origin;
	}

	public IState<T> getTarget() {
		return target;
	}

	public IChecker<T> getChecker() {
		return checker;
	}
}
