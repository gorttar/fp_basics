/*
 * Copyright (c) 2008-2018 Maxifier Ltd. All Rights Reserved.
 */

import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * @author Andrey Antipov (andrey.antipov@cxense.com) (2018-06-08 20:41)
 */
public interface YCombinator {
    @FunctionalInterface
    interface F<A, B> extends Function<A, B> {
        default B a(A a) {
            return apply(a);
        }
    }

    @FunctionalInterface
    interface RF<Fun> extends F<RF<Fun>, Fun> {
    }

    static <A, B> F<A, B> y(F<F<A, B>, F<A, B>> f) {
        RF<F<A, B>> rf = (RF<F<A, B>> w) -> f.a(a -> {
            final F<A, B> a1 = w.a(w);
            return a1.a(a);
        });
        return rf.a(rf);
    }

    static void main(String[] args) {
        F<Integer, Integer> factorial = y(f -> n -> (n <= 1) ? 1 : n * f.a(n - 1));
        F<Integer, Integer> fibonacci = y(f -> n -> (n <= 2) ? 1 : f.a(n - 1) + f.a(n - 2));
        System.out.print("Factorial(1..10)   : ");
        IntStream.rangeClosed(1, 10).forEach(i -> System.out.printf("%s ", factorial.a(i)));
        System.out.print("\nFibonacci(1..10)   : ");
        IntStream.rangeClosed(1, 10).forEach(i -> System.out.printf("%s ", fibonacci.a(i)));
        System.out.println();
    }
}

