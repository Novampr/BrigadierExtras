# Brigadier Extras
Brigadier Extras provides many utilities for using Brigadier including an annotation based command system, all translated into Brigadier, it even has adapters for PaperMC and FabricMC.

Example Fabric usage:
```java
// MyMod.java
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            CommandBuilder.registerCommand(commandDispatcher, CommandSourceStack.class, new MyCommand());
        });
    }
}

// MyCommand.java
@Command("command")
public class MyCommand {
    @Path
    public int noArgs(CommandSourceStack commandSourceStack) {
        commandSourceStack.sendSystemMessage(Component.literal("My command has been executed!"));
        return Command.SUCCESS;
    }

    @Path
    public int addition(CommandSourceStack commandSourceStack, Integer integer1, Integer integer2) {
        commandSourceStack.sendSystemMessage(Component.literal("Addition output:"));
        commandSourceStack.sendSystemMessage(Component.literal(integer1 + " + " + integer2 + " = " + (integer1 + integer2)));
        return Command.SUCCESS;
    }

    @Path
    @Permission("my.command.subtraction")
    public int subtraction(CommandSourceStack commandSourceStack, Literal subtract, Integer integer1, Integer integer2) {
        commandSourceStack.sendSystemMessage(Component.literal("Subtraction output:"));
        commandSourceStack.sendSystemMessage(Component.literal(integer1 + " - " + integer2 + " = " + (integer1 - integer2)));
        return Command.SUCCESS;
    }

    @OP
    @Path
    public int op(CommandSourceStack commandSourceStack, Literal op) {
        commandSourceStack.sendSystemMessage(Component.literal("I'm OP, I'm all powerful!"));
        return Command.SUCCESS;
    }
}
```