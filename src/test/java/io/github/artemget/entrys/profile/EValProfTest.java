/*
 * MIT License
 *
 * Copyright (c) 2024-2025. Artem Getmanskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.artemget.entrys.profile;

import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.fake.EFake;
import io.github.artemget.entrys.fake.EFakeErr;
import io.github.artemget.entrys.file.EVal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;

/**
 * Test cases for {@link io.github.artemget.entrys.profile.EValProf}.
 *
 * @since 0.4.0
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods"})
final class EValProfTest {

    @Test
    void throwsAtProfileIsBlank() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EValProf("entrys.profile", new EFakeErr<>(), null).value(),
            "Profile node is blank"
        );
    }

    @Test
    void throwsAtEmptyContent() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EValProf("abc", new EFakeErr<>(), null).value(),
            "Didnt throw at error getting content"
        );
    }

    @Test
    void parsesString() throws EntryException {
        Assertions.assertEquals(
            "123",
            new EValProf("age", new EFake<>("age: \"123\""), null).value(),
            "String not parsed"
        );
    }

    @Test
    void parsesInteger() throws EntryException {
        Assertions.assertEquals(
            Integer.MAX_VALUE,
            Integer.parseInt(
                new EValProf(
                    "age",
                    new EFake<>(String.format("age: %s", Integer.MAX_VALUE)),
                    null
                ).value()
            ),
            "Integer not parsed"
        );
    }

    @Test
    void parsesLong() throws EntryException {
        Assertions.assertEquals(
            Long.MAX_VALUE,
            Long.parseLong(
                new EValProf(
                    "age",
                    new EFake<>(String.format("age: %s", Long.MAX_VALUE)),
                    null
                ).value()
            ),
            "Long not parsed"
        );
    }

    @Test
    void parsesFloat() throws EntryException {
        Assertions.assertEquals(
            Float.MAX_VALUE,
            Float.parseFloat(
                new EValProf(
                    "age",
                    new EFake<>(
                        String.format("age: %s", Float.MAX_VALUE)
                    ),
                    null
                ).value()
            ),
            "Float not parsed"
        );
    }

    @Test
    void parsesBoolean() throws EntryException {
        Assertions.assertTrue(
            Boolean.parseBoolean(
                new EValProf(
                    "age",
                    new EFake<>(
                        "age: true"
                    ),
                    null
                ).value()
            ),
            "Boolean not parsed"
        );
    }

    @Test
    void throwsAtNullValue() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EValProf("age", new EFake<>("age: null"), null).value(),
            "Didnt throw at null value"
        );
    }

    @Test
    void throwsAtNullAttribute() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EValProf("age", new EFake<>("age2: null"), null).value(),
            "Didnt throw at null value"
        );
    }

    @Test
    void parsesInnerNode() throws EntryException {
        Assertions.assertEquals(
            "123",
            new EValProf(
                "person.age",
                new EFake<>(
                    "person:\n  age: \"123\""
                ),
                null
            ).value(),
            "Inner node not parsed"
        );
    }

    @Test
    void parsesArray() throws EntryException {
        Assertions.assertEquals(
            "123;321",
            new EValProf("ages", new EFake<>("ages: [ 123, 321 ]"), null).value(),
            "Array node not parsed"
        );
    }

    @Test
    void parsesEmptyArray() throws EntryException {
        Assertions.assertEquals(
            "",
            new EValProf(
                "ages",
                new EFake<>(
                    "ages: []"
                ),
                null
            ).value(),
            "Empty array not parsed"
        );
    }

    @Test
    void parsesAnotherArray() throws EntryException {
        Assertions.assertEquals(
            "123;321",
            new EValProf(
                "ages",
                new EFake<>("ages:\n  - 123\n  - 321"),
                null
            ).value(),
            "Array not parsed"
        );
    }

    @Test
    void parsesStringWrap() throws EntryException {
        Assertions.assertEquals(
            " First line.\n Second line.\n",
            new EVal(
                "description",
                new EFake<>("description: >\n First line.\n Second line.")
            ).value(),
            "String wrap not parsed"
        );
    }

    @Test
    void parsesDefaultValueAtMissingEnv() throws EntryException {
        Assertions.assertEquals(
            "123",
            new EValProf(
                "age",
                new EFake<>(
                    "age: ${my_env:123}"
                ),
                null
            ).value(),
            "Not parsed default value"
        );
    }

    @Test
    void parsesDefaultValueWithDelimiterAtMissingEnv() throws EntryException {
        Assertions.assertEquals(
            "https://192.168.0.1:8080",
            new EValProf(
                "age",
                new EFake<>(
                    "age: ${my_env:https://192.168.0.1:8080}"
                ),
                null
            ).value(),
            "Not parsed default value with delimiter"
        );
    }

    @Test
    void parsesEnv() throws Exception {
        Assertions.assertEquals(
            "321",
            new EnvironmentVariables("my_env", "321").execute(
                () -> new EValProf("age", new EFake<>("age: ${my_env}"), null).value()
            ),
            "Not parsed default value"
        );
    }

    @Test
    void parsesEnvOverwritesDefaultValue() throws Exception {
        Assertions.assertEquals(
            "111",
            new EnvironmentVariables("my_env", "111").execute(
                () -> new EValProf("age", new EFake<>("age: ${my_env:123}"), null).value()
            ),
            "Not parsed default value"
        );
    }
}
