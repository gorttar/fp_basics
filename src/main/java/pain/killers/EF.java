/*
 * Copyright (c) 2018 Andrey Antipov. All Rights Reserved.
 */
package pain.killers;

/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-06-10)
 */
@FunctionalInterface
public interface EF<
        A, B,
        T extends Throwable> extends EFT<A, B> {
    B a(A a) throws T;
}