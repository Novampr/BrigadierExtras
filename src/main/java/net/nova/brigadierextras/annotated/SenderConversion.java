package net.nova.brigadierextras.annotated;

public interface SenderConversion<Source, Result> {
    Class<Source> getSourceSender();
    Class<Result> getResultSender();

    SenderData<Result> convert(Source sender);
}
