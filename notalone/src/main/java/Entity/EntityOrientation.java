package Entity;

public enum EntityOrientation {
    left(-1),
    right(1);

    private final int value;

    EntityOrientation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
