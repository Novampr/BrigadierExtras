package net.nova.brigadierextras;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.nova.brigadierextras.annotated.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class CommandBuilder {
    private static final Map<Class<?>, ArgumentType<?>> ARGUMENTS = new HashMap<>();
    private static final Map<Class<?>, Function<Object, ?>> MODIFIERS = new HashMap<>();

    private static final Set<BranchModifier> BUILDER_MODIFIERS = new HashSet<>();
    private static final Set<RootModifier> ROOT_MODIFIERS = new HashSet<>();

    public static <S> void registerCommand(CommandDispatcher<S> dispatcher, Class<S> dispatcherClass, Object command) throws InvalidCommandException {
        Class<?> clazz = command.getClass();

        if (!clazz.isAnnotationPresent(Command.class)) throw new InvalidCommandException("Command must be annotated with the Command annotation.");

        Command command1 = clazz.getAnnotation(Command.class);

        String name = command1.value();

        if (!name.matches("[a-zA-Z]+")) throw new InvalidCommandException("Command must only have alphabetical characters in the name.");

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Path.class)) {
                if (method.getReturnType() != int.class && method.getReturnType() != Integer.class) {
                    throw new InvalidCommandException("Method must return an integer as a command path.");
                }

                if (!method.getParameters()[0].getType().equals(dispatcherClass)) {
                    throw new InvalidCommandException("First parameter must be dispatcher source.");
                }

                if (method.getParameters().length == 1) {
                    //noinspection unchecked
                    dispatcher.register(
                            (LiteralArgumentBuilder<S>) LiteralArgumentBuilder.literal(name)
                                    .executes(ctx -> {
                                        try {
                                            return (int) method.invoke(command, ctx.getSource());
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                    );
                    return;
                }

                method.setAccessible(true);
                List<Parameter> originalParameters = Arrays.asList(method.getParameters());

                List<Parameter> parameters = new ArrayList<>(originalParameters);
                parameters.removeFirst();
                List<Parameter> reversedParameters = new ArrayList<>(parameters);
                Collections.reverse(reversedParameters);

                Parameter parameter = reversedParameters.getFirst();

                ArgumentBuilder<S, ?> argumentBuilder;

                Class<?> type = parameter.getType();

                if (type == Literal.class) {
                    argumentBuilder = LiteralArgumentBuilder.literal(parameter.getName());
                } else if (type.isAssignableFrom(Enum.class) && !ARGUMENTS.containsKey(type)) {
                    Enum<?>[] enums = (Enum<?>[]) type.getEnumConstants();

                    //noinspection unchecked
                    argumentBuilder = (ArgumentBuilder<S, ?>) RequiredArgumentBuilder.argument(parameter.getName(), StringArgumentType.word())
                            .suggests((context, builder) -> {
                                for (Enum<?> enu : enums) {
                                    builder.suggest(enu.name());
                                }
                                return builder.buildFuture();
                            });
                } else {
                    if (!ARGUMENTS.containsKey(type))
                        throw new InvalidCommandException("Invalid argument type given.");

                    argumentBuilder = RequiredArgumentBuilder.argument(parameter.getName(), ARGUMENTS.get(type));
                }

                List<BranchModifier> modifiers = new ArrayList<>(BUILDER_MODIFIERS);

                modifiers.sort(Comparator.comparingInt(BranchModifier::priority));

                for (BranchModifier branchModifier : modifiers) {
                    argumentBuilder = branchModifier.function().modify(argumentBuilder, method, clazz);
                }

                argumentBuilder = argumentBuilder.executes((ctx) -> {
                    List<Object> providedArguments = new ArrayList<>();

                    providedArguments.add(ctx.getSource());

                    for (Parameter parameter1 : parameters) {
                        Class<?> claz = parameter1.getType();

                        if (claz == Literal.class) {
                            providedArguments.add(new Literal(parameter1.getName()));
                        } else if (claz.isAssignableFrom(Enum.class) && !ARGUMENTS.containsKey(claz)) {
                            Enum<?>[] enums = (Enum<?>[]) claz.getEnumConstants();
                            boolean added = false;
                            String value = ctx.getArgument(parameter1.getName(), String.class);
                            for (Enum<?> enu : enums) {
                                if (enu.name().equals(value)) {
                                    providedArguments.add(enu);
                                    added = true;
                                }
                            }
                            if (!added) throw new InvalidCommandException("Invalid enum constant.");
                        } else {
                            if (!ARGUMENTS.containsKey(claz))
                                throw new InvalidCommandException("Invalid argument type given.");

                            if (MODIFIERS.containsKey(claz)) {
                                providedArguments.add(MODIFIERS.get(claz).apply(
                                        ctx.getArgument(parameter1.getName(), claz))
                                );
                            } else {
                                providedArguments.add(ctx.getArgument(parameter1.getName(), claz));
                            }
                        }
                    }

                    try {
                        return (int) method.invoke(command, providedArguments.toArray());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

                reversedParameters.removeFirst();

                for (Parameter p : reversedParameters) {
                    Class<?> aClass = p.getType();

                    if (aClass == Literal.class) {
                        if (argumentBuilder != null) {
                            //noinspection unchecked
                            argumentBuilder = (ArgumentBuilder<S, ?>) LiteralArgumentBuilder.literal(p.getName()).then((ArgumentBuilder<Object, ?>) argumentBuilder);
                        } else {
                            argumentBuilder = LiteralArgumentBuilder.literal(p.getName());
                        }
                    } else if (aClass.isAssignableFrom(Enum.class) && !ARGUMENTS.containsKey(aClass)) {
                        Enum<?>[] enums = (Enum<?>[]) type.getEnumConstants();
                        if (argumentBuilder != null) {
                            //noinspection unchecked
                            argumentBuilder = (ArgumentBuilder<S, ?>) RequiredArgumentBuilder.argument(parameter.getName(), StringArgumentType.word())
                                    .suggests((context, builder) -> {
                                        for (Enum<?> enu : enums) {
                                            builder.suggest(enu.name());
                                        }
                                        return builder.buildFuture();
                                    })
                                    .then((ArgumentBuilder<Object, ?>) argumentBuilder);
                        } else {
                            //noinspection unchecked
                            argumentBuilder = (ArgumentBuilder<S, ?>) RequiredArgumentBuilder.argument(parameter.getName(), StringArgumentType.word())
                                    .suggests((context, builder) -> {
                                        for (Enum<?> enu : enums) {
                                            builder.suggest(enu.name());
                                        }
                                        return builder.buildFuture();
                                    });
                        }
                    } else {
                        if (!ARGUMENTS.containsKey(aClass))
                            throw new InvalidCommandException("Invalid argument type given.");

                        if (argumentBuilder != null) {
                            //noinspection unchecked
                            argumentBuilder = (ArgumentBuilder<S, ?>) RequiredArgumentBuilder.argument(p.getName(), ARGUMENTS.get(aClass)).then((ArgumentBuilder<Object, ?>) argumentBuilder);
                        } else {
                            argumentBuilder = RequiredArgumentBuilder.argument(p.getName(), ARGUMENTS.get(aClass));
                        }
                    }

                    for (BranchModifier branchModifier : modifiers) {
                        argumentBuilder = branchModifier.function().modify(argumentBuilder, method, clazz);
                    }
                }

                //noinspection unchecked
                LiteralArgumentBuilder<S> root = (LiteralArgumentBuilder<S>) LiteralArgumentBuilder.literal(name)
                        .then((ArgumentBuilder<Object, ?>) argumentBuilder);

                List<RootModifier> rootModifiers = new ArrayList<>(ROOT_MODIFIERS);

                rootModifiers.sort(Comparator.comparingInt(RootModifier::priority));

                for (RootModifier rootModifier : rootModifiers) {
                    root = rootModifier.function().modify(root, clazz);
                }

                dispatcher.register(root);
            }
        }
    }

    public static <T> boolean registerArgument(Class<T> type, ArgumentType<T> argumentType) {
        return registerArgument(type, argumentType, false);
    }

    public static <T> boolean registerArgument(Class<T> type, ArgumentType<T> argumentType, boolean force) {
        if (ARGUMENTS.containsKey(type)) {
            if (!force) return false;
        }

        ARGUMENTS.put(type, argumentType);
        return true;
    }

    public static <T, S> boolean registerArgument(Class<T> type, ArgumentType<S> argumentType, Function<S, T> function) {
        return registerArgument(type, argumentType, function, false);
    }

    public static <T, S> boolean registerArgument(Class<T> type, ArgumentType<S> argumentType, Function<S, T> function, boolean force) {
        if (MODIFIERS.containsKey(type) ) {
            if (!force) return false;
        }

        ARGUMENTS.put(type, argumentType);
        //noinspection unchecked
        MODIFIERS.put(type, (Function<Object, ?>) function);
        return true;
    }

    public static void registerBuilderModifier(BranchModifier branchModifier) {
        BUILDER_MODIFIERS.add(branchModifier);
    }

    public static void registerRootModifier(RootModifier rootModifier) {
        ROOT_MODIFIERS.add(rootModifier);
    }
}
