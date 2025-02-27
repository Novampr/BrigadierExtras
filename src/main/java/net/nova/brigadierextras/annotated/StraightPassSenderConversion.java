package net.nova.brigadierextras.annotated;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class StraightPassSenderConversion<T> implements SenderConversion<T, T> {
    private final Class<T> SENDER_CLAZZ;

    public StraightPassSenderConversion(Class<T> SENDER_CLAZZ) {
        this.SENDER_CLAZZ = SENDER_CLAZZ;
    }

    @Override
    public Class<T> getSourceSender() {
        return SENDER_CLAZZ;
    }

    @Override
    public Class<T> getResultSender() {
        return SENDER_CLAZZ;
    }

    @Override
    public SenderData<T> convert(T sender) {
        return SenderData.ofSender(sender);
    }
}
