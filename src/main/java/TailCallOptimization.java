/*
 * Copyright (c) 2008-2018 Maxifier Ltd. All Rights Reserved.
 */

import java.util.function.Supplier;

/**
 * @author Andrey Antipov (andrey.antipov@cxense.com) (2018-06-08 21:39)
 */
public class TailCallOptimization {
    private static TCFunction<Integer, Boolean> even;
    private static TCFunction<Integer, Boolean> odd;

    public static void main(String[] args) {
        even = n -> n == 0 ? ret(true) : ap(n - 1, odd);
        odd = n -> n == 0 ? ret(false) : ap(n - 1, even);
        System.out.println(even.a(10).a()); // корректно напечатает true
        System.out.println(even.a(10_000_000).a()); // корректно напечатает true
        System.out.println(odd.a(10).a()); // корректно напечатает false
        System.out.println(odd.a(10_000_000).a()); // корректно напечатает false
    }

    @SuppressWarnings("WeakerAccess")
    public static <A, B> TCResult<B> ap(A a, TCFunction<A, B> f) {
        return new Call<>(a, f);
    }

    @SuppressWarnings("WeakerAccess")
    public static <B> TCResult<B> ret(B b) {
        return () -> b;
    }

    @SuppressWarnings("WeakerAccess")
    @FunctionalInterface
    public interface TCFunction<A, B> extends YCombinator.F<A, TCResult<B>> {
    }

    @FunctionalInterface
    public interface TCResult<B> extends Supplier<B> {
        default B a() {
            return get();
        }
    }

    private static final class Call<A, B> implements TCResult<B> {
        private final A a;
        private final TCFunction<A, B> nextStep;

        Call(A a, TCFunction<A, B> nextStep) {
            this.a = a;
            this.nextStep = nextStep;
        }

        @Override
        public B get() {
            var step = next();
            //noinspection StatementWithEmptyBody
            for (; step instanceof Call; step = ((Call<A, B>) step).next()) {
            }
            return step.a();
        }

        TCResult<B> next() {
            return nextStep.a(a);
        }
    }
}
