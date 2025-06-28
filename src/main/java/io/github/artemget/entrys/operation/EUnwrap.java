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

import io.github.artemget.entrys.ESafe;
import io.github.artemget.entrys.Entry;
import io.github.artemget.entrys.EntryException;

/**
 * Unwraps value between prefix and suffix.
 *
 * @since 0.4.0
 */
public final class EUnwrap implements Entry<String> {
    /**
     * Origin entry.
     */
    private final Entry<String> origin;

    /**
     * Ctor with default '{}' wraps.
     *
     * @param value String itself
     */
    public EUnwrap(final String value) {
        this(value, "{", "}");
    }

    /**
     * Ctor with custom suffix and prefix.
     *
     * @param value String itself
     * @param prefix Of string
     * @param suffix Of string
     */
    public EUnwrap(final String value, final String prefix, final String suffix) {
        this(
            new ESafe<>(
                () -> {
                    final String unwrapped;
                    try {
                        final int start = value.indexOf(prefix) + prefix.length();
                        unwrapped = value.substring(
                            start,
                            value.indexOf(suffix, start)
                        );
                    } catch (final IndexOutOfBoundsException exception) {
                        throw new EntryException(
                            String.format(
                                "Failed to unwrap value:'%s' with prefix:'%s' and suffix:'%s'",
                                value,
                                prefix,
                                suffix
                            ),
                            exception
                        );
                    }
                    return unwrapped;
                }
            )
        );
    }

    /**
     * Main ctor.
     * @param origin Entry
     */
    private EUnwrap(final Entry<String> origin) {
        this.origin = origin;
    }

    @Override
    public String value() throws EntryException {
        return this.origin.value();
    }
}
