package org.jwebconsole.client.util.monad.trymonad;

import org.jwebconsole.client.util.FunctionUtils;
import org.jwebconsole.client.util.monad.option.Option;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<T> {

    public static <R> Try<R> create(Supplier<R> supplier) {
        try {
            return new Success<R>(supplier.get());
        } catch (Throwable e) {
            return new Failure<R>(e);
        }
    }

    public default <R> Try<R> map(Function<T, R> function) {
        if (this.isSuccess()) {
            try {
                return new Success<>(function.apply(this.get()));
            } catch (Exception e) {
                return new Failure<>(e);
            }
        } else {
            return new Failure<>(this.getCause());
        }
    }


    public default <R> void forEach(Consumer<T> consumer) {
        this.map(FunctionUtils.consumerToFunction(consumer));
    }

    public default <R> Try<R> flatMap(Function<T, Try<R>> function) {
        if (this.isSuccess()) {
            try {
                Try<R> result = function.apply(this.get());
                if (result.isSuccess()) return new Success<>(result.get());
                else return new Failure<R>(result.getCause());
            } catch (Throwable e) {
                return new Failure<R>(e);
            }
        } else {
            return new Failure<R>(this.getCause());
        }

    }

    Option<T> toOption();

    boolean isSuccess();

    T get();

    Throwable getCause();

}




