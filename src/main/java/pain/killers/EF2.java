/*
 * Copyright (c) 2018 Andrey Antipov. All Rights Reserved.
 */
package pain.killers;

/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-06-10)
 */
@FunctionalInterface
public interface EF2<
        A, B,
        T extends Throwable,
        T2 extends Throwable> {
    B a(A a) throws T, T2;
}