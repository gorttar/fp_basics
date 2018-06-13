/*
 * Copyright (c) 2018 Andrey Antipov. All Rights Reserved.
 */
package pain.killers;

import java.util.function.Function;

/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-06-10)
 */
public final class LThrow {
    private LThrow() {
    }

    private static <A, T extends Throwable> A st(Throwable t) throws T {
        //noinspection unchecked
        throw (T) t;
    }

    public static <A, B,
            T extends Throwable> Function<A, B> fThrows(
            EF<
                    ? super A, ? extends B,
                    T> ef)
            throws T {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable> Function<A, B> fThrows2(
            EF2<
                    ? super A, ? extends B,
                    T, T2> ef)
            throws T, T2 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable> Function<A, B> fThrows3(
            EF3<
                    ? super A, ? extends B,
                    T, T2, T3> ef)
            throws T, T2, T3 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable,
            T4 extends Throwable> Function<A, B> fThrows4(
            EF4<
                    ? super A, ? extends B,
                    T, T2, T3, T4> ef)
            throws T, T2, T3, T4 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable,
            T4 extends Throwable,
            T5 extends Throwable> Function<A, B> fThrows5(
            EF5<
                    ? super A, ? extends B,
                    T, T2, T3, T4, T5> ef)
            throws T, T2, T3, T4, T5 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable,
            T4 extends Throwable,
            T5 extends Throwable,
            T6 extends Throwable> Function<A, B> fThrows6(
            EF6<
                    ? super A, ? extends B,
                    T, T2, T3, T4, T5, T6> ef)
            throws T, T2, T3, T4, T5, T6 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable,
            T4 extends Throwable,
            T5 extends Throwable,
            T6 extends Throwable,
            T7 extends Throwable> Function<A, B> fThrows7(
            EF7<
                    ? super A, ? extends B,
                    T, T2, T3, T4, T5, T6, T7> ef)
            throws T, T2, T3, T4, T5, T6, T7 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable,
            T4 extends Throwable,
            T5 extends Throwable,
            T6 extends Throwable,
            T7 extends Throwable,
            T8 extends Throwable> Function<A, B> fThrows8(
            EF8<
                    ? super A, ? extends B,
                    T, T2, T3, T4, T5, T6, T7, T8> ef)
            throws T, T2, T3, T4, T5, T6, T7, T8 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable,
            T4 extends Throwable,
            T5 extends Throwable,
            T6 extends Throwable,
            T7 extends Throwable,
            T8 extends Throwable,
            T9 extends Throwable> Function<A, B> fThrows9(
            EF9<
                    ? super A, ? extends B,
                    T, T2, T3, T4, T5, T6, T7, T8, T9> ef)
            throws T, T2, T3, T4, T5, T6, T7, T8, T9 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }

    public static <A, B,
            T extends Throwable,
            T2 extends Throwable,
            T3 extends Throwable,
            T4 extends Throwable,
            T5 extends Throwable,
            T6 extends Throwable,
            T7 extends Throwable,
            T8 extends Throwable,
            T9 extends Throwable,
            T10 extends Throwable> Function<A, B> fThrows10(
            EF10<
                    ? super A, ? extends B,
                    T, T2, T3, T4, T5, T6, T7, T8, T9, T10> ef)
            throws T, T2, T3, T4, T5, T6, T7, T8, T9, T10 {
        return a -> {
            try {
                return ef.a(a);
            } catch (Throwable t) {
                return st(t);
            }
        };
    }
}