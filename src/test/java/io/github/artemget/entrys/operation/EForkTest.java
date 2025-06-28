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

package io.github.artemget.entrys.operation;

import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.fake.EFake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link EFork}.
 * @since 0.4.0
 */
class EForkTest {

    @Test
    void returnsOrigin() throws EntryException {
        Assertions.assertEquals(
            "123",
            new EFork<>(
                () -> true,
                new EFake<>("123"),
                new EFake<>("321")
            ).value(),
            "Returned spare"
        );
    }

    @Test
    void returnsSpare() throws EntryException {
        Assertions.assertEquals(
            "321",
            new EFork<>(
                () -> false,
                new EFake<>("123"),
                new EFake<>("321")
            ).value(),
            "Returned origin"
        );
    }
}