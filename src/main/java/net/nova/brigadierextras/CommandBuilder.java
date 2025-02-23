package net.nova.brigadierextras;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.nova.brigadierextras.annotated.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public class CommandBuilder {
    public static final SimpleCommandExceptionType INVALID_ENUM = new SimpleCommandExceptionType(() -> "Invalid enum constant.");

    private static final Map<Class<?>, ArgumentType<?>> ARGUMENTS = new HashMap<>();
    private static final Map<Class<?>, Function<Object, ?>> MODIFIERS = new HashMap<>();

    private static final Set<Resolver<?, ?>> RESOLVERS = new HashSet<>();

    private static final Set<BranchModifier> BUILDER_MODIFIERS = new HashSet<>();
    private static final Set<RootModifier> ROOT_MODIFIERS = new HashSet<>();

    public static <S> void registerCommand(CommandDispatcher<S> dispatcher, Class<S> dispatcherClass, Object command) throws InvalidCommandException {
        registerCommand(dispatcher, dispatcherClass, dispatcherClass, sender -> sender, command);
    }

    public static <S, T> void registerCommand(CommandDispatcher<S> dispatcher, Class<T> dispatcherClass, Class<S> actualDispatcherClass, Function<S, ? extends T> conv, Object command) throws InvalidCommandException {
        for (LiteralArgumentBuilder<S> builtCommand : buildCommand(dispatcherClass, actualDispatcherClass, conv, command)) {
            dispatcher.register(builtCommand);
        }
    }

    public static <S> List<LiteralArgumentBuilder<S>> buildCommand(Class<S> dispatcherClass, Object command) throws InvalidCommandException {
        return buildCommand(dispatcherClass, dispatcherClass, sender -> sender, command);
    }

    public static <S, T> List<LiteralArgumentBuilder<S>> buildCommand(Class<T> dispatcherClass, Class<S> actualDispatcherClass, Function<S, ? extends T> conv, Object command) throws InvalidCommandException {
        List<LiteralArgumentBuilder<S>> builtCommands = new ArrayList<>();

        Class<?> clazz = command.getClass();

        if (!clazz.isAnnotationPresent(Command.class))
            throw new InvalidCommandException("Command must be annotated with the Command annotation.");

        Command command1 = clazz.getAnnotation(Command.class);

        String name = command1.value();

        if (!name.matches("[a-zA-Z]+"))
            throw new InvalidCommandException("Command must only have alphabetical characters in the name.");

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Path.class)) {
                if (method.getReturnType() != int.class && method.getReturnType() != Integer.class && method.getReturnType() != Status.class) {
                    throw new InvalidCommandException("Method must return an integer or status as a command path.");
                }

                if (method.getParameters().length == 0) {
                    throw new InvalidCommandException("Path must have dispatcher source be the first parameter, current path has no parameters.");
                }

                if (!method.getParameters()[0].getType().equals(dispatcherClass)) {
                    throw new InvalidCommandException("First parameter must be dispatcher source.");
                }

                method.setAccessible(true);

                if (method.getParameters().length == 1) {
                    //noinspection unchecked
                    builtCommands.add((LiteralArgumentBuilder<S>) LiteralArgumentBuilder.literal(name)
                            .executes(ctx -> {
                                try {
                                    //noinspection unchecked
                                    Object obj = method.invoke(command, conv.apply((S) ctx.getSource()));
                                    return method.getReturnType() == Status.class ? ((Status) obj).getNum() : (int) obj;
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }));
                    continue;
                }

                List<Parameter> originalParameters = Arrays.asList(method.getParameters());

                List<Parameter> parameters = new ArrayList<>(originalParameters);
                parameters.removeFirst();
                List<Parameter> reversedParameters = new ArrayList<>(parameters);
                Collections.reverse(reversedParameters);

                Parameter parameter = reversedParameters.getFirst();

                ArgumentBuilder<S, ?> argumentBuilder = null;

                Class<?> type = parameter.getType();

                if (type.isAssignableFrom(Literal.class)) {
                    argumentBuilder = LiteralArgumentBuilder.literal(parameter.getName());
                } else if (type.isEnum() && !ARGUMENTS.containsKey(type)) {
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
                    if (!ARGUMENTS.containsKey(type)) {
                        boolean skip = false;

                        for (Resolver<?, ?> resolver : RESOLVERS) {
                            if (!resolver.getExpectedSenderClass().equals(actualDispatcherClass)) continue;

                            if (resolver.getArgumentClass().equals(type)) {
                                argumentBuilder = (ArgumentBuilder<S, ?>) resolver.generateArgumentBuilder(parameter.getName());
                                skip = true;
                            }
                        }

                        if (!skip) throw new InvalidCommandException("Invalid argument type given.");
                    } else {
                        argumentBuilder = RequiredArgumentBuilder.argument(parameter.getName(), ARGUMENTS.get(type));
                    }
                }

                List<BranchModifier> modifiers = new ArrayList<>(BUILDER_MODIFIERS);

                modifiers.sort(Comparator.comparingInt(BranchModifier::priority));

                for (BranchModifier branchModifier : modifiers) {
                    argumentBuilder = branchModifier.function().modify(argumentBuilder, method, clazz);
                }

                argumentBuilder = argumentBuilder.executes((ctx) -> {
                    List<Object> providedArguments = new ArrayList<>();

                    providedArguments.add(conv.apply(ctx.getSource()));

                    for (Parameter parameter1 : parameters) {
                        Class<?> claz = parameter1.getType();

                        if (claz.isAssignableFrom(Literal.class)) {
                            providedArguments.add(new Literal(parameter1.getName()));
                        } else if (claz.isEnum() && !ARGUMENTS.containsKey(claz)) {
                            Enum<?>[] enums = (Enum<?>[]) claz.getEnumConstants();
                            boolean added = false;
                            String value = ctx.getArgument(parameter1.getName(), String.class);
                            for (Enum<?> enu : enums) {
                                if (enu.name().equals(value)) {
                                    providedArguments.add(enu);
                                    added = true;
                                }
                            }
                            if (!added) throw INVALID_ENUM.create();
                        } else {
                            if (!ARGUMENTS.containsKey(claz)) {
                                boolean skip = false;

                                for (Resolver<?, ?> resolver : RESOLVERS) {
                                    if (!resolver.getExpectedSenderClass().equals(actualDispatcherClass)) continue;

                                    if (resolver.getArgumentClass().equals(claz)) {
                                        providedArguments.add(((Resolver<?, S>) resolver).getType(ctx, parameter1.getName()));
                                        skip = true;
                                    }
                                }

                                if (!skip) throw new InvalidCommandException("Invalid argument type given.");
                            } else if (MODIFIERS.containsKey(claz)) {
                                providedArguments.add(MODIFIERS.get(claz).apply(
                                        ctx.getArgument(parameter1.getName(), claz))
                                );
                            } else {
                                providedArguments.add(ctx.getArgument(parameter1.getName(), claz));
                            }
                        }
                    }

                    try {
                        Object obj = method.invoke(command, providedArguments.toArray());
                        return method.getReturnType() == Status.class ? ((Status) obj).getNum() : (int) obj;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

                reversedParameters.removeFirst();

                for (Parameter p : reversedParameters) {
                    Class<?> aClass = p.getType();

                    if (aClass.isAssignableFrom(Literal.class)) {
                        if (argumentBuilder != null) {
                            //noinspection unchecked
                            argumentBuilder = (ArgumentBuilder<S, ?>) LiteralArgumentBuilder.literal(p.getName()).then((ArgumentBuilder<Object, ?>) argumentBuilder);
                        } else {
                            argumentBuilder = LiteralArgumentBuilder.literal(p.getName());
                        }
                    } else if (aClass.isEnum() && !ARGUMENTS.containsKey(aClass)) {
                        Enum<?>[] enums = (Enum<?>[]) aClass.getEnumConstants();
                        if (argumentBuilder != null) {
                            //noinspection unchecked
                            argumentBuilder = (ArgumentBuilder<S, ?>) RequiredArgumentBuilder.argument(p.getName(), StringArgumentType.word())
                                    .suggests((context, builder) -> {
                                        for (Enum<?> enu : enums) {
                                            builder.suggest(enu.name());
                                        }
                                        return builder.buildFuture();
                                    })
                                    .then((ArgumentBuilder<Object, ?>) argumentBuilder);
                        } else {
                            //noinspection unchecked
                            argumentBuilder = (ArgumentBuilder<S, ?>) RequiredArgumentBuilder.argument(p.getName(), StringArgumentType.word())
                                    .suggests((context, builder) -> {
                                        for (Enum<?> enu : enums) {
                                            builder.suggest(enu.name());
                                        }
                                        return builder.buildFuture();
                                    });
                        }
                    } else if (!ARGUMENTS.containsKey(aClass)) {
                        boolean skip = false;

                        for (Resolver<?, ?> resolver : RESOLVERS) {
                            if (!resolver.getExpectedSenderClass().equals(actualDispatcherClass)) continue;

                            if (resolver.getArgumentClass().equals(aClass)) {
                                if (argumentBuilder != null) {
                                    //noinspection unchecked
                                    argumentBuilder = ((Resolver<?, S>) resolver).generateArgumentBuilder(p.getName()).then(argumentBuilder);
                                } else {
                                    //noinspection unchecked
                                    argumentBuilder = ((Resolver<?, S>) resolver).generateArgumentBuilder(p.getName());
                                }
                                skip = true;
                            }
                        }

                        if (!skip) throw new InvalidCommandException("Invalid argument type given.");
                    } else if (argumentBuilder != null) {
                        //noinspection unchecked
                        argumentBuilder = (ArgumentBuilder<S, ?>) RequiredArgumentBuilder.argument(p.getName(), ARGUMENTS.get(aClass)).then((ArgumentBuilder<Object, ?>) argumentBuilder);
                    } else {
                        argumentBuilder = RequiredArgumentBuilder.argument(p.getName(), ARGUMENTS.get(aClass));
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

                builtCommands.add(root);
            }
        }

        return Collections.unmodifiableList(builtCommands);
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

    public static <S extends Annotation> void registerAnnotationModifier(AnnotationModifier<S> annotationModifier) {
        BUILDER_MODIFIERS.add(new BranchModifier(annotationModifier.priority(), new BranchModifier.Handler() {
            @Override
            public <T> ArgumentBuilder<T, ?> modify(ArgumentBuilder<T, ?> argumentBuilder, Method method, Class<?> clazz) {
                if (method.isAnnotationPresent(annotationModifier.annotationClass())) {
                    return (ArgumentBuilder<T, ?>) annotationModifier.handler().modify(argumentBuilder, method.getAnnotation(annotationModifier.annotationClass()));
                }

                return argumentBuilder;
            }
        }));

        ROOT_MODIFIERS.add(new RootModifier(annotationModifier.priority(), new RootModifier.Handler() {
            @Override
            public <T> LiteralArgumentBuilder<T> modify(LiteralArgumentBuilder<T> argumentBuilder, Class<?> clazz) {
                if (clazz.isAnnotationPresent(annotationModifier.annotationClass())) {
                    return (LiteralArgumentBuilder<T>) annotationModifier.handler().modify(argumentBuilder, clazz.getAnnotation(annotationModifier.annotationClass()));
                }

                return argumentBuilder;
            }
        }));
    }

    public static <T, S> void registerResolver(Resolver<T, S> resolver) {
        RESOLVERS.add(resolver);
    }
}
