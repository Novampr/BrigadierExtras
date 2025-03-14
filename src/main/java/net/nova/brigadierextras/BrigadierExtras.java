package net.nova.brigadierextras;

import com.mojang.brigadier.arguments.*;
import net.nova.brigadierextras.annotated.GreedyString;
import net.nova.brigadierextras.annotated.Negative;
import net.nova.brigadierextras.annotated.Positive;
import net.nova.brigadierextras.annotated.SingleWord;

public class BrigadierExtras {
    public static void init() {
        CommandBuilder.registerArgument(Integer.class, IntegerArgumentType.integer());
        CommandBuilder.registerArgument(Positive.Integer.class, Integer.class, IntegerArgumentType.integer(0), Positive.Integer::new);
        CommandBuilder.registerArgument(Negative.Integer.class, Integer.class, IntegerArgumentType.integer(Integer.MIN_VALUE, 0), Negative.Integer::new);

        CommandBuilder.registerArgument(Long.class, LongArgumentType.longArg());
        CommandBuilder.registerArgument(Positive.Long.class, Long.class, LongArgumentType.longArg(0), Positive.Long::new);
        CommandBuilder.registerArgument(Negative.Long.class, Long.class, LongArgumentType.longArg(Long.MIN_VALUE, 0), Negative.Long::new);

        CommandBuilder.registerArgument(Float.class, FloatArgumentType.floatArg());
        CommandBuilder.registerArgument(Positive.Float.class, Float.class, FloatArgumentType.floatArg(0), Positive.Float::new);
        CommandBuilder.registerArgument(Negative.Float.class, Float.class, FloatArgumentType.floatArg(Float.MIN_VALUE, 0), Negative.Float::new);

        CommandBuilder.registerArgument(Double.class, DoubleArgumentType.doubleArg());
        CommandBuilder.registerArgument(Positive.Double.class, Double.class, DoubleArgumentType.doubleArg(0), Positive.Double::new);
        CommandBuilder.registerArgument(Negative.Double.class, Double.class, DoubleArgumentType.doubleArg(Double.MIN_VALUE, 0), Negative.Double::new);

        CommandBuilder.registerArgument(Boolean.class, BoolArgumentType.bool());

        CommandBuilder.registerArgument(String.class, StringArgumentType.string());
        CommandBuilder.registerArgument(GreedyString.class, String.class, StringArgumentType.greedyString(), GreedyString::new);
        CommandBuilder.registerArgument(SingleWord.class, String.class, StringArgumentType.word(), SingleWord::new);
    }
}
