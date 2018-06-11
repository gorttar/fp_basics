/*
 * Copyright (c) 2018 Andrey Antipov. All Rights Reserved.
 */
package pain.killers;

/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-06-10)
 */
@FunctionalInterface
public interface EF3<
        A, B,
        T extends Throwable,
        T2 extends Throwable,
        T3 extends Throwable> extends EFT<A, B> {
    @Override
    B a(A a) throws T, T2, T3;
}