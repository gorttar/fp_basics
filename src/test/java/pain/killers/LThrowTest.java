package pain.killers;

import static pain.killers.LThrow.fThrows;

import org.testng.annotations.Test;

/**
 * @author Andrey Antipov (gorttar@gmail.com) (2018-06-10)
 */
public class LThrowTest {
    private class E extends Throwable {
    }

    private class E2 extends Throwable {
    }

    private class E3 extends Throwable {
    }

    private class E4 extends Throwable {
    }

    private class E5 extends Throwable {
    }

    private class E6 extends Throwable {
    }

    private class E7 extends Throwable {
    }

    private class E8 extends Throwable {
    }

    private class E9 extends Throwable {
    }

    private class E10 extends Throwable {
    }

    @Test(expectedExceptions = E.class)
    public void testFThrows() throws E {
        fThrows(
                x -> {
                    throw new E();
                }).apply(null);
    }

    @Test(expectedExceptions = E.class)
    public void testFThrows2E() throws E, E2 {
        LThrow.
                // а здесь тупые долбоклюи, которые пишут выведвение типов в компиляторе Java,
                // оказались неспособны сами вывести, что будет бросаться только E или E2,
                // поэтому приходится писать сигнатуру явно
                        <String, Void, E, E2>
                        fThrows2(
                        x -> {
                            if (false) throw new E2();
                            throw new E();
                        }).apply(null);
    }

    @Test(expectedExceptions = E2.class)
    public void testFThrows2E2() throws E, E2 {
        LThrow.
                <String, Void, E, E2>
                        fThrows2(
                        x -> {
                            if (false) throw new E();
                            throw new E2();
                        }).apply(null);
    }

    @Test(expectedExceptions = E.class)
    public void testFThrows3E() throws E, E2, E3 {
        LThrow.
                <String, Void, E, E2, E3>
                        fThrows3(
                        x -> {
                            if (false) throw new E2();
                            if (false) throw new E3();
                            throw new E();
                        }).apply(null);
    }

    @Test(expectedExceptions = E2.class)
    public void testFThrows3E2() throws E, E2, E3 {
        LThrow.
                <String, Void, E, E2, E3>
                        fThrows3(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E3();
                            throw new E2();
                        }).apply(null);
    }

    @Test(expectedExceptions = E3.class)
    public void testFThrows3E3() throws E, E2, E3 {
        LThrow.
                <String, Void, E, E2, E3>
                        fThrows3(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E2();
                            throw new E3();
                        }).apply(null);
    }

    @Test(expectedExceptions = E.class)
    public void testFThrows4E() throws E, E2, E3, E4 {
        LThrow.
                <String, Void, E, E2, E3, E4>
                        fThrows4(
                        x -> {
                            if (false) throw new E2();
                            if (false) throw new E3();
                            if (false) throw new E4();
                            throw new E();
                        }).apply(null);
    }

    @Test(expectedExceptions = E2.class)
    public void testFThrows4E2() throws E, E2, E3, E4 {
        LThrow.
                <String, Void, E, E2, E3, E4>
                        fThrows4(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E3();
                            if (false) throw new E4();
                            throw new E2();
                        }).apply(null);
    }

    @Test(expectedExceptions = E3.class)
    public void testFThrows4E3() throws E, E2, E3, E4 {
        LThrow.
                <String, Void, E, E2, E3, E4>
                        fThrows4(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E2();
                            if (false) throw new E4();
                            throw new E3();
                        }).apply(null);
    }

    @Test(expectedExceptions = E4.class)
    public void testFThrows4E4() throws E, E2, E3, E4 {
        LThrow.
                <String, Void, E, E2, E3, E4>
                        fThrows4(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E2();
                            if (false) throw new E3();
                            throw new E4();
                        }).apply(null);
    }

    @Test(expectedExceptions = E.class)
    public void testFThrows5E() throws E, E2, E3, E4, E5 {
        LThrow.
                <String, Void, E, E2, E3, E4, E5>
                        fThrows5(
                        x -> {
                            if (false) throw new E2();
                            if (false) throw new E3();
                            if (false) throw new E4();
                            if (false) throw new E5();
                            throw new E();
                        }).apply(null);
    }

    @Test(expectedExceptions = E2.class)
    public void testFThrows5E2() throws E, E2, E3, E4, E5 {
        LThrow.
                <String, Void, E, E2, E3, E4, E5>
                        fThrows5(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E3();
                            if (false) throw new E4();
                            if (false) throw new E5();
                            throw new E2();
                        }).apply(null);
    }

    @Test(expectedExceptions = E3.class)
    public void testFThrows5E3() throws E, E2, E3, E4, E5 {
        LThrow.
                <String, Void, E, E2, E3, E4, E5>
                        fThrows5(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E2();
                            if (false) throw new E4();
                            if (false) throw new E5();
                            throw new E3();
                        }).apply(null);
    }

    @Test(expectedExceptions = E4.class)
    public void testFThrows5E4() throws E, E2, E3, E4, E5 {
        LThrow.
                <String, Void, E, E2, E3, E4, E5>
                        fThrows5(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E2();
                            if (false) throw new E3();
                            if (false) throw new E5();
                            throw new E4();
                        }).apply(null);
    }

    @Test(expectedExceptions = E5.class)
    public void testFThrows5E5() throws E, E2, E3, E4, E5 {
        LThrow.
                <String, Void, E, E2, E3, E4, E5>
                        fThrows5(
                        x -> {
                            if (false) throw new E();
                            if (false) throw new E2();
                            if (false) throw new E3();
                            if (false) throw new E4();
                            throw new E5();
                        }).apply(null);
    }
}