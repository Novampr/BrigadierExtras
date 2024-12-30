package net.nova.brigadierextras.fabric.test;

import java.util.function.BiFunction;

public enum Type {
    ADD("+", (num1, num2) -> num1 + num2),
    SUB("-", (num1, num2) -> num1 - num2),
    DIV("/", (num1, num2) -> num1 / num2),
    MUL("*", (num1, num2) -> num1 * num2);

    public String getSign() {
        return sign;
    }

    public BiFunction<Float, Float, Float> getTask() {
        return task;
    }

    private final String sign;
    private final BiFunction<Float, Float, Float> task;

    Type(String sign, BiFunction<Float, Float, Float> task) {
        this.sign = sign;
        this.task = task;
    }
}
