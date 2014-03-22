package org.jwebconsole.client.util.monad.trymonad;

import org.jwebconsole.client.util.monad.option.Option;

import java.util.NoSuchElementException;

public class Success<T> implements Try<T> {

    private T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public Option<T> toOption() {
        return Option.create(value);
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Throwable getCause() {
        throw new NoSuchElementException();
    }

    @Override
    public String toString() {
        return "Success{" +
                "value=" + value +
                '}';
    }
}