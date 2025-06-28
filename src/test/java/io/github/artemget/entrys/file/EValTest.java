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

package io.github.artemget.entrys.file;

import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.fake.EFake;
import io.github.artemget.entrys.fake.EFakeErr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link io.github.artemget.entrys.json.EJsonArr}.
 * @since 0.4.0
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
final class EValTest {

    @Test
    void throwsAtEmptyContent() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EVal("abc", new EFakeErr<>()).value(),
            "Didnt throw at error getting content"
        );
    }

    @Test
    void parsesString() throws EntryException {
        Assertions.assertEquals(
            "123",
            new EVal("age", new EFake<>("age: \"123\"")).value(),
            "String not parsed"
        );
    }

    @Test
    void parsesInteger() throws EntryException {
        Assertions.assertEquals(
            Integer.MAX_VALUE,
            Integer.parseInt(
                new EVal("age", new EFake<>(String.format("age: %s", Integer.MAX_VALUE))).value()
            ),
            "Integer not parsed"
        );
    }

    @Test
    void parsesLong() throws EntryException {
        Assertions.assertEquals(
            Long.MAX_VALUE,
            Long.parseLong(
                new EVal("age", new EFake<>(String.format("age: %s", Long.MAX_VALUE))).value()
            ),
            "Long not parsed"
        );
    }

    @Test
    void parsesFloat() throws EntryException {
        Assertions.assertEquals(
            Float.MAX_VALUE,
            Float.parseFloat(
                new EVal("age", new EFake<>(String.format("age: %s", Float.MAX_VALUE))).value()
            ),
            "Float not parsed"
        );
    }

    @Test
    void parsesBoolean() throws EntryException {
        Assertions.assertTrue(
            Boolean.parseBoolean(new EVal("age", new EFake<>("age: true")).value()),
            "Boolean not parsed"
        );
    }

    @Test
    void throwsAtNullValue() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EVal("age", new EFake<>("age: null")).value(),
            "Didnt throw at null value"
        );
    }

    @Test
    void throwsAtNullAttribute() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EVal("age", new EFake<>("age2: null")).value(),
            "Didnt throw at null value"
        );
    }

    @Test
    void parsesInnerNode() throws EntryException {
        Assertions.assertEquals(
            "123",
            new EVal(
                "person.age",
                new EFake<>(
                    """
                        person:
                         age: "123"
                        """
                )
            ).value(),
            "Inner node not parsed"
        );
    }

    @Test
    void parsesArray() throws EntryException {
        Assertions.assertEquals(
            "123;321",
            new EVal(
                "ages",
                new EFake<>(
                    "ages: [ 123, 321 ]"
                )
            ).value(),
            "Array node not parsed"
        );
    }

    @Test
    void parsesEmptyArray() throws EntryException {
        Assertions.assertEquals(
            "",
            new EVal(
                "ages",
                new EFake<>(
                    "ages: []"
                )
            ).value(),
            "Empty array not parsed"
        );
    }

    @Test
    void parsesAnotherArray() throws EntryException {
        Assertions.assertEquals(
            "123;321",
            new EVal(
                "ages",
                new EFake<>(
                    """
                        ages:
                          - 123
                          - 321
                        """
                )
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
                new EFake<>(
                    "description: >\n First line.\n Second line."
                )
            ).value(),
            "String wrap not parsed"
        );
    }
}
