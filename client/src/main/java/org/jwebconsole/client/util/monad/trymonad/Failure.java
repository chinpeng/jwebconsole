package org.jwebconsole.client.util.monad.trymonad;

import org.jwebconsole.client.util.monad.option.Option;

import java.util.NoSuchElementException;

public class Failure<T> implements Try<T> {

    private Throwable cause;

    public Failure(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public Option<T> toOption() {
        return Option.getEmpty();
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T get() {
        throw new NoSuchElementException();
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return "Failure{" +
                "cause=" + cause +
                '}';
    }
}
