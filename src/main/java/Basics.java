/*
 * Copyright (c) 2008-2018 Maxifier Ltd. All Rights Reserved.
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Andrey Antipov (andrey.antipov@cxense.com) (2018-06-08 17:30)
 */
public class Basics {
    public static void main(String[] args) {
        // Компактный синтаксис λ-функций?
        final Function<String, String> fx = x -> x + "foo";
        final Function<String, String> fx1 = x -> {
            var y = "foo";
            return x + y;
        };

        // λ-функции являются синтаксическим сахаром и не имеют собственного типа
        final Function<Boolean, Boolean> p1 = x -> !x; // а вот так можно
        final Predicate<Boolean> p2 = x -> !x; // не может использоваться вместо p1, так как другой тип
        final Function<Boolean, Boolean> p3 = p2::test; // а вот так можно

        Stream.of("foo", "bar")
                .map(pathname -> new File(pathname))
                .map(file -> file.toPath())
                .flatMap(
                        path -> {
                            try {
                                return Files.lines(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }) // а вот так можно
                .forEach(line -> System.out.println(line));

        // сигнатуры функций высших порядков в Java выглядят слегка многословными
        Function<String, Function<String, String>> hof = x -> y -> x + y + x;
        // частичное применение в Java очень многословно
        hof.apply("foo").apply("bar");
    }
}