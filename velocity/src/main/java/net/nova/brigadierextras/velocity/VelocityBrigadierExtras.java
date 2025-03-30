package net.nova.brigadierextras.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.nova.brigadierextras.BrigadierExtras;
import net.nova.brigadierextras.CommandBuilder;
import net.nova.brigadierextras.annotated.AnnotationModifier;
import net.nova.brigadierextras.velocity.annotated.Permission;
import net.nova.brigadierextras.velocity.resolvers.PlayerResolver;
import net.nova.brigadierextras.velocity.resolvers.RegisteredServerResolver;
import net.nova.brigadierextras.velocity.senders.ConsoleSender;
import net.nova.brigadierextras.velocity.senders.PlayerSender;
import org.slf4j.Logger;

@Plugin(
    id = "brigadierextras",
    name = "BrigadierExtras",
    version = BuildConstants.VERSION,
    authors = "Nova"
)
public class VelocityBrigadierExtras {
    protected static VelocityBrigadierExtras INSTANCE;
    public static VelocityBrigadierExtras getInstance() {
        return INSTANCE;
    }

    public final ProxyServer proxy;

    @Inject
    public VelocityBrigadierExtras(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        INSTANCE = this;

        BrigadierExtras.init();
        logger.debug("Initialized BrigadierExtras");

        CommandBuilder.registerResolver(new PlayerResolver());
        logger.debug("Registered PlayerResolver");
        CommandBuilder.registerResolver(new RegisteredServerResolver());
        logger.debug("Registered RegisteredServerResolver");

        CommandBuilder.registerAnnotationModifier(
                new AnnotationModifier<>(
                        1,
                        Permission.class,
                        (argumentBuilder, permission) ->
                                argumentBuilder.requires(
                                        sender -> ((CommandSource) sender).hasPermission(permission.value())
                                )
                )
        );
        logger.debug("Registered Permission AnnotationModifier");

        CommandBuilder.registerSenderConversion(new PlayerSender());
        logger.debug("Registered PlayerSender SenderConversion");

        CommandBuilder.registerSenderConversion(new ConsoleSender());
        logger.debug("Registered ConsoleSender SenderConversion");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
