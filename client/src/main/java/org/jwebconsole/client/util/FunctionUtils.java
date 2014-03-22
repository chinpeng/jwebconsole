package org.jwebconsole.client.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionUtils {

    public static <T> Function<T, Void> consumerToFunction(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return null;
        };
    }

}
