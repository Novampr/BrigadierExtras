package net.nova.brigadierextras.annotated;

@SuppressWarnings("ClassCanBeRecord")
public class Literal {
    private final String value;

    public String value() {
        return value;
    }

    public Literal(String value) {
        this.value = value;
    }
}
