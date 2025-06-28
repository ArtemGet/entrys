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

import io.github.artemget.entrys.ESafe;
import io.github.artemget.entrys.EntryException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * File's content entry.
 *
 * @since 0.4.0
 */
public class EFile extends ESafe<String> {
    public EFile(final String path) {
        this(path, StandardCharsets.UTF_8);
    }

    public EFile(final String path, final Charset charset) {
        super(
            () -> {
                try {
                    return Files.readString(Path.of(path), charset);
                } catch (final IOException | SecurityException exception) {
                    throw new EntryException(
                        String.format(
                            "Failed to load contents of file for path: '%s'",
                            path
                        ),
                        exception
                    );
                }
            },
            () -> String.format("Empty file for path: '%s'", path)
        );
    }
}
