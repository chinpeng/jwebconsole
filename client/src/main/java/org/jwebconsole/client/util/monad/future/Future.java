package org.jwebconsole.client.util.monad.future;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jwebconsole.client.util.monad.option.Option;
import org.jwebconsole.client.util.monad.trymonad.Failure;
import org.jwebconsole.client.util.monad.trymonad.Success;
import org.jwebconsole.client.util.monad.trymonad.Try;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Future<T> {

    private List<Consumer<Try<T>>> completeFunctions = new ArrayList<>();
    private Option<Try<T>> result = Option.getEmpty();

    public Future() {
    }

    public Future(Consumer<MethodCallback<T>> consumer) {
        consumer.accept(toCallback());
    }

    public void onComplete(Consumer<Try<T>> consumer) {
        result.forEach(consumer::accept);
        completeFunctions.add(consumer);
    }

    public void handle(Consumer<Throwable> errorHandler, Consumer<T> successHandler) {
        onComplete((result) -> {
            if (result.isSuccess()) successHandler.accept(result.get());
            else errorHandler.accept(result.getCause());
        });
    }

    public void completeWithResult(Try<T> result) {
        this.result = Option.create(result);
        for (Consumer<Try<T>> completeFunction : completeFunctions) {
            completeFunction.accept(result);
        }
    }

    public void completeWithSuccess(T result) {
        completeWithResult(new Success<T>(result));
    }

    public MethodCallback<T> toCallback() {
        return new MethodCallback<T>() {
            @Override
            public void onFailure(Method method, Throwable e) {
                completeWithResult(new Failure<T>(e));
            }

            @Override
            public void onSuccess(Method method, T response) {
                completeWithResult(new Success<T>(response));
            }
        };
    }

    public void forEach(Consumer<T> consumer) {
        onComplete((result) -> {
            if (result.isSuccess()) {
                consumer.accept(result.get());
            }
        });
    }

    public <R> Future<R> map(Function<T, R> function) {
        Future<R> future = new Future<R>();
        onComplete((result) -> {
            if (result.isSuccess()) {
                future.completeWithSuccess(function.apply(result.get()));
            }
        });
        return future;
    }

    public <R> Future<R> flatMap(Function<T, Future<R>> function) {
        Future<R> mapped = new Future<R>();
        onComplete((result) -> {
            if (result.isSuccess()) {
                Future<R> f = function.apply(result.get());
                f.onComplete(mapped::completeWithResult);
            }
        });
        return mapped;
    }

}
