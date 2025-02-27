package net.nova.brigadierextras.annotated;

public interface SenderConversion<T, S> {
    Class<T> getSourceSender();
    Class<S> getResultSender();

    SenderData<S> convert(T sender);
}
