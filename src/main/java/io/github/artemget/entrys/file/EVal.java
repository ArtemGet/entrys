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

import com.amihaiemil.eoyaml.Node;
import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import io.github.artemget.entrys.ESafe;
import io.github.artemget.entrys.Entry;
import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.operation.EContains;
import io.github.artemget.entrys.operation.EFork;
import io.github.artemget.entrys.operation.ESplit;
import io.github.artemget.entrys.operation.EUnwrap;
import io.github.artemget.entrys.system.EEnv;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
            () -> {
                YamlMapping root;
                try {
                    root = Yaml.createYamlInput(content.value())
                        .readYamlMapping();
                } catch (final IOException | EntryException exception) {
                    throw new EntryException(
                        String.format("Failed to read yaml mapping for key: '%s'", key),
                        exception
                    );
                }
                String res = null;
                final List<String> elements = new ESplit(() -> key, ".").value();
                for (int element = 0; element < elements.size(); ++element) {
                    if (element == elements.size() - 1) {
                        final YamlNode value = root.value(elements.get(element));
                        if (Node.SCALAR == value.type()) {
                            res = EVal.ejected(value);
                        } else if (Node.SEQUENCE == value.type()) {
                            res = value.asSequence()
                                .children().stream()
                                .map(node -> node.asScalar().value())
                                .collect(Collectors.joining(";"));
                        }
                    }
                    root = root.yamlMapping(elements.get(element));
                }
                return res;
            },
            () -> String.format("Attribute for key '%s' is null", key)
        );
    }

    private static String ejected(final YamlNode node) throws EntryException {
        final String scalar = node.asScalar().value();
        return new EFork<>(
            () -> scalar.startsWith("${") && scalar.endsWith("}"),
            new EFork<>(
                () -> new ESplit(() -> scalar, ":").value().size() >= 2,
                new EFork<>(
                    new EContains(new EEnv(() -> EVal.selectedEnv(scalar).trim())),
                    new EEnv(() -> EVal.selectedEnv(scalar).trim()),
                    () -> EVal.joined(scalar)
                ),
                new EEnv(new EUnwrap(scalar, "${", "}"))
            ),
            () -> scalar
        ).value();
    }

    private static String selectedEnv(final String scalar) throws EntryException {
        return new ESplit(new EUnwrap(scalar, "${", "}"), ":")
            .value().get(0);
    }

    private static String joined(final String scalar) throws EntryException {
        final List<String> split = new ESplit(
            new EUnwrap(scalar, "${", "}"), ":"
        ).value();
        final StringBuilder value = new StringBuilder();
        for (int index = 1; index < split.size(); ++index) {
            value.append(split.get(index));
            if (index < split.size() - 1) {
                value.append(':');
            }
        }
        return value.toString();
    }
}
