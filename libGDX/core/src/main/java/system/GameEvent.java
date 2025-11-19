package system;

public class GameEvent {
    private final String type;
    private final Object data;

    public GameEvent(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GameEvent{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }

    public Object getVolume() {
        return  data;
    }
}
