package sadaguro.poker8.statemachine;

import sadaguro.poker8.interfacesImpl.IState;

public class AfterStateDecorator<T> implements IState<T> {
	private final IState<T> state;
	private final Runnable listener;

	public AfterStateDecorator(IState<T> state, Runnable listener) {
		this.state = state;
		this.listener = listener;
	}

	@Override
	public String getName() {
		return state.getName();
	}

	@Override
	public boolean execute(T context) {
		boolean result = state.execute(context);
		if (result) {
			listener.run();
		}
		return result;
	}
}
