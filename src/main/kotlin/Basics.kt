import java.lang.Math.random

@Suppress("NAME_SHADOWING")
fun main(args: Array<String>) {
    val x = "x"
    val y = "y"
    // свободные переменные (захват из лексического контекста)
    println(x + y); // x и y - свободные переменные, предоставляемые внешним контекстом

    // λ-абстракция (функция из выражения)
    // λx.expression
    { x: String -> x + y } // связывание переменной х с параметром функции, у - свободная переменная
    { x: String -> { y: String -> x + y } } // связывание переменных х и y с параметрами функции, свободных переменных нет

    // β-редукция (аппликация)
    println({ x: String -> x + y }("foo")) // = "fooy"
    println({ x: String -> { y: String -> x + y } }("foo")("bar")); // = "foobar"

    // α-эквивалентность
    println((1..100)
            .map { _ -> random() * 1000 }.map(Double::toInt)
            .all { x -> { x: Int -> x + 1 }(x) == { y: Int -> y + 1 }(x) });

    // функции высших порядков
    { f: (String) -> String -> f("qaz") + "asd" } // функция, параметром которой является другая функция
    println({ f: (String) -> String -> f("qaz") + "asd" }({ x: String -> x + x })); // = "qazqazasd"
    { x: String -> { y: String -> x + y } } // функция, возвращающая другую функцию
    println({ x: String -> { y: String -> x + y } }("foo")("baz")) // = "foobaz"

    // η-преобразование
    println((1..100)
            .map { _ -> random() * 1000 }.map(Double::toInt)
            .all { x -> { y: Int -> y * y }(x) == { x: Int -> { y: Int -> y * y }(x) }(x) })

    // рекурсия
    val fac: (Any) -> (Int) -> Int = { r ->
        { n ->
            if (n == 0) 1
            else {
                val rf = (r as (Any) -> (Int) -> Int)(r)
                n * rf(n - 1)
            }
        }
    }
    val factorial = fac(fac) // применив fac к fac получаем рекурсивную функцию
    println(factorial(10)) // = 3628800
    val fib: (Any) -> (Int) -> Int = { r ->
        { n ->
            if (n <= 2) 1
            else {
                val rf = (r as (Any) -> (Int) -> Int)(r)
                rf(n - 1) + rf(n - 2)
            }
        }
    }
    val fibonacci = fib(fib) // применив fib к fib получаем рекурсивную функцию
    println(fibonacci(10)); // = 55

    // каррирование и частичное применение
    // функция от параметров x и y в каррированном виде:
    // функция от x, возвращающая функцию от y
    { x: String -> { y: String -> x + y } }
    { x: String -> { y: String -> x + y } }("foo") // функция от y
    println({ x: String -> { y: String -> x + y } }("foo")("baz")); // = "foobaz"

    // Захват переменной из контекста
    {
        val x = "foo"
        val fy = { y: String -> x + y } // нельзя вынести за пределы блока, так как есть захват x из контекста блока
        fy("baz")
    }
    {
        val x = "bar"
        val fy = { y: String -> x + y } // нельзя вынести за пределы блока, так как есть захват x из контекста
        fy("qaz")
    }

    // Частичное применение
    val fxy = { x: String -> { y: String -> x + y } } // нет свободных переменных. Можно объявить где угодно в коде
    {
        val fy = fxy("foo")
        fy("baz")
    }
    {
        val fy = fxy("bar")
        fy("qaz")
    }
}