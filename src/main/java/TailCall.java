/*
 * Copyright (c) 2008-2018 Maxifier Ltd. All Rights Reserved.
 */

import java.util.function.Predicate;

/**
 * @author Andrey Antipov (andrey.antipov@cxense.com) (2018-06-08 17:52)
 */
public class TailCall {
    static Predicate<Integer> odd;
    static Predicate<Integer> even;

    public static void main(String[] args) {
        even = n -> n == 0 || odd.test(n - 1);
        odd = n -> n != 0 && even.test(n - 1);
        System.out.println(even.test(10)); // корректно напечатает true
        System.out.println(even.test(10_000_000)); // упадёт со StackOverflowError
    }
}