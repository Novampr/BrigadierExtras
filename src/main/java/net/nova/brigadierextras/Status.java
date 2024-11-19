package net.nova.brigadierextras;

public enum Status {
    SUCCESS(1),
    FAILURE(0);

    public int getNum() {
        return num;
    }

    private final int num;

    Status(int num) {
        this.num = num;
    }
}
