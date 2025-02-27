package net.nova.brigadierextras.annotated;

import net.nova.brigadierextras.Status;

public class SenderData<S> {
    public S getSender() {
        return sender;
    }

    public int getStatusCode() {
        return statusCode;
    }

    private S sender = null;
    private int statusCode = 0;

    private SenderData() {}

    /**
     * Create a SenderData instance with a functional sender
     * @param sender The command sender to pass into the command
     * @return a success SenderData
     */
    public static <S> SenderData<S> ofSender(S sender) {
        SenderData<S> senderData = new SenderData<>();
        senderData.sender = sender;
        return senderData;
    }

    /**
     * Create a SenderData instance with a status code to instantly return,
     * indicating the brigadier source did not meet certain requirements to be used in a command.
     * For example:
     * A player only command ran by a command block
     * @param code The status code to exit the command with
     * @return a failed SenderData
     */
    public static <S> SenderData<S> ofFailed(int code) {
        SenderData<S> senderData = new SenderData<>();
        senderData.statusCode = code;
        return senderData;
    }

    /**
     * Create a SenderData instance with a status code to instantly return,
     * indicating the brigadier source did not meet certain requirements to be used in a command.
     * For example:
     * A player only command ran by a command block
     * @param code The status code to exit the command with
     * @return a failed SenderData
     */
    public static <S> SenderData<S> ofFailed(Status code) {
        SenderData<S> senderData = new SenderData<>();
        senderData.statusCode = code.getNum();
        return senderData;
    }

    /**
     * Create a SenderData instance with a status code to instantly return,
     * indicating the brigadier source did not meet certain requirements to be used in a command.
     * For example:
     * A player only command ran by a command block
     * @return a failed SenderData
     */
    public static <S> SenderData<S> ofFailed() {
        return new SenderData<>();
    }
}
