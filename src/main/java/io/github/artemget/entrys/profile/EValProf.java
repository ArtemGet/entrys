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

import io.github.artemget.entrys.ESafe;
import io.github.artemget.entrys.Entry;
import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.file.EFile;
import io.github.artemget.entrys.file.EVal;

/**
 * Configuration properties entry.
 * By default gets entries from "src/main/resources/application.yaml"
 * Supports all yaml types, default values and envs passed via ${ENV}.
 *
 * @since 0.4.0
 */
public final class EValProf extends ESafe<String> {

    /**
     * From yaml file at default dir.
     *
     * @param key Of Entry
     */
    public EValProf(final String key) {
        this(key, "src/main/resources/application.yaml");
    }

    /**
     * From yaml file.
     *
     * @param key Of Entry
     * @param path To Yaml File
     */
    public EValProf(final String key, final String path) {
        this(key, new EFile(path), path);
    }

    /**
     * Main ctor.
     *
     * @param key Of Entry
     * @param content Yaml Content
     * @param path Yaml Path
     */
    public EValProf(final String key, final Entry<String> content, final String path) {
        super(
            () -> {
                String result;
                final String profile;
                try {
                    profile = new EVal("entrys.profile", path).value();
                    if (profile.isBlank()) {
                        throw new EntryException("Attribute for key 'profile' is empty");
                    } else {
                        result = new EVal(key, parsePath(path, profile)).value();
                    }
                } catch (final EntryException exception) {
                    result = new EVal(key, content).value();
                }
                return result;
            },
            () -> String.format("Attribute for key '%s' is null", key)
        );
    }

    private static String parsePath(final String path, final String profile) {
        return path.replace("application", String.format("application%s", profile));
    }
}
