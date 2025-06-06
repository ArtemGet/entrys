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
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Split entry.
 * @since 0.0.2
 */
public final class ESplit implements Entry<List<String>> {
    /**
     * Origin string entry.
     */
    private final Entry<String> origin;

    /**
     * Split delimiter.
     */
    private final String delimiter;

    /**
     * Creates split entry with default ; delimiter.
     * @param origin Entry
     */
    public ESplit(final Entry<String> origin) {
        this(origin, ";");
    }

    /**
     * Main ctor.
     * @param origin Entry
     * @param delimiter For splitting
     */
    public ESplit(final Entry<String> origin, final String delimiter) {
        this.origin = new ESafe<>(origin);
        this.delimiter = delimiter;
    }

    @Override
    public List<String> value() throws EntryException {
        final String value = this.origin.value();
        try {
            return List.of(value.split(Pattern.quote(this.delimiter)));
        } catch (final PatternSyntaxException exception) {
            throw new EntryException(
                String.format(
                    "Wrong pattern delimiter: %s for entry value: %s",
                    this.delimiter,
                    value
                ),
                exception
            );
        }
    }
}
