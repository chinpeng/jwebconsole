package org.jwebconsole.client.util.monad.option;

import org.jwebconsole.client.util.FunctionUtils;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Option<T> {

    public static final Option<?> EMPTY = new None<>();

    public <R> Option<R> map(Function<T, R> function) {
        if (this.isEmpty()) {
            return getEmpty();
        } else {
            R result = function.apply(this.get());
            if (result == null) {
                return getEmpty();
            } else {
                return new Some<R>(result);
            }
        }
    }

    public <R> Option<R> flatMap(Function<T, Option<R>> function) {
        if (this.isEmpty())  {
            return getEmpty();
        } else {
            Option<R> result = function.apply(this.get());
            if (result.isEmpty()) {
                return getEmpty();
            } else {
                return function.apply(get());
            }

        }
    }

    public <R> void forEach(Consumer<T> consumer) {
        this.map(FunctionUtils.consumerToFunction(consumer));
    }

    public static <E> Option<E> getEmpty() {
        return (Option<E>) EMPTY;
    }

    public static <R> Option<R> create(R value) {
        if (value == null) return getEmpty();
        else return new Some<R>(value);
    }

    abstract boolean isEmpty();

    abstract T get();

    private static class Some<E> extends Option<E>{

        private E value;

        public Some(E value) {
            this.value = value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public E get() {
            return value;
        }

        @Override
        public String toString() {
            return "Some{" +
                    "value=" + value +
                    '}';
        }
    }

    private static class None<E> extends Option<E>{

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public E get() {
            throw new NoSuchElementException();
        }

        @Override
        public String toString() {
            return "None";
        }
    }

}





