/*
 * Copyright (c) 2018 Andrey Antipov. All Rights Reserved.
 */
package pain.killers;

/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-06-10)
 */
@FunctionalInterface
public interface EF10<
        A, B,
        T extends Throwable,
        T2 extends Throwable,
        T3 extends Throwable,
        T4 extends Throwable,
        T5 extends Throwable,
        T6 extends Throwable,
        T7 extends Throwable,
        T8 extends Throwable,
        T9 extends Throwable,
        T10 extends Throwable> {
    B a(A a) throws T, T2, T3, T4, T5, T6, T7, T8, T9, T10;
}