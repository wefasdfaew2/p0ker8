package sadaguro.poker8.statemachine;

import sadaguro.poker8.interfacesImpl.IState;

public class BeforeStateDecorator<T> implements IState<T> {
	private final IState<T> state;
	private final Runnable listener;
	private boolean executed = true;

	public BeforeStateDecorator(IState<T> state, Runnable listener) {
		this.state = state;
		this.listener = listener;
	}

	@Override
	public String getName() {
		return state.getName();
	}

	@Override
	public boolean execute(T context) {
		if (executed) {
			listener.run();
		}
		executed = state.execute(context);
		return executed;
	}
}
