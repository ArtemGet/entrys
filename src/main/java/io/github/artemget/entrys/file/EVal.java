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
import io.github.artemget.entrys.Entry;
import io.github.artemget.entrys.yaml.EYaml;

/**
 * Configuration properties entry.
 * By default gets entries from "src/main/resources/application.yaml"
 * Supports all yaml types, default values and envs passed via ${ENV}.
 *
 * @since 0.4.0
 */
public final class EVal extends ESafe<String> {

    /**
     * From yaml file at default dir.
     *
     * @param key Of entry
     */
    public EVal(final String key) {
        this(key, "src/main/resources/application.yaml");
    }

    /**
     * From yaml file.
     *
     * @param key Of entry
     * @param path To yaml file
     */
    public EVal(final String key, final String path) {
        this(key, new EFile(path));
    }

    /**
     * Main ctor.
     *
     * @param key Of Entry
     * @param content Yaml content
     */
    @SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
    public EVal(final String key, final Entry<String> content) {
        super(
            () -> new EYaml(key, content).value(),
            () -> String.format("Attribute for key '%s' is null", key)
        );
    }
}
