package sadaguro.poker8.dispatcher;

public class GameEvent {
	private String type;
	private String source;
	private Object payload;

	public GameEvent() {
	}

	public GameEvent(String type, String source) {
		this.source = source;
		this.type = type;
	}

	public GameEvent(String type, String source, Object payload) {
		this.source = source;
		this.type = type;
		this.payload = payload;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
