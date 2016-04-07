package sadaguro.poker8.statemachine;

import sadaguro.poker8.interfacesImpl.IState;

public class StateMachineInstance<T> {

	private final T context;
	private final StateMachine<T> parent;
	private IState<T> state;
	private boolean finish;
	private boolean pause;

	public StateMachineInstance(T context, StateMachine<T> parent, IState<T> state) {
		this.context = context;
		this.parent = parent;
		this.state = state;
		this.finish = false;
	}

	public boolean isFinish() {
		return finish;
	}

	public StateMachineInstance<T> execute() {
		this.pause = false;
		while (state != null && !pause) {
			state = executeState();
		}
		finish = state == null;
		return this;
	}

	public T getContext() {
		return context;
	}

	private IState<T> executeState() {
		pause = !state.execute(context);
		IState<T> result = state;
		if (!pause) {
			for (Transition<T> transition : parent.getTransitionsByOrigin(state)) {
				if (transition.getChecker().check(context)) {
					return transition.getTarget();
				}
			}
			result = parent.getDefaultTransition(state);
		}
		return result;
	}

}
