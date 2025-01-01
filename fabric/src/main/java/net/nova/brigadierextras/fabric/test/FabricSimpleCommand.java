package net.nova.brigadierextras.fabric.test;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.nova.brigadierextras.SimpleCommand;
import net.nova.brigadierextras.Status;
import org.jetbrains.annotations.NotNull;

public class FabricSimpleCommand implements SimpleCommand<CommandSourceStack> {
    @Override
    public String getName() {
        return "mysimplecommand";
    }

    @Override
    public Status executeCommand(@NotNull CommandContext<CommandSourceStack> context, @NotNull String input) {
        if (input.equals("Hello!")) {
            context.getSource().sendSystemMessage(Component.literal("Hello there!"));
        } else {
            context.getSource().sendSystemMessage(Component.literal("You didn't even say hello. >:("));
        }

        return Status.SUCCESS;
    }
}
