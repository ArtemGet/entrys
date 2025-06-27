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

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import io.github.artemget.entrys.ESafe;
import io.github.artemget.entrys.Entry;
import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.operation.ESplit;
import java.io.IOException;
import java.util.List;

public final class EVal extends ESafe<String> {

    public EVal(final String key) {
        this(key, "src/main/resources/application.yaml");
    }

    public EVal(final String key, final String path) {
        this(key, new EFile(path));
    }

    public EVal(final String key, final Entry<String> content) {
        super(
            () -> {
                YamlMapping root;
                try {
                    root = Yaml.createYamlInput(content.value())
                        .readYamlMapping();
                } catch (IOException | EntryException exception) {
                    throw new EntryException(String.format("Failed to read yaml mapping for key: '%s'", key), exception);
                }
                String res = null;
                List<String> elements = new ESplit(() -> key, ".").value();
                for (int i = 0; i < elements.size(); i++) {
                    if (i == elements.size() - 1) {
                        final YamlNode value = root.value(elements.get(i));
                        switch (value.type()) {
                            case SCALAR ->  res = value.toString();
                            case SEQUENCE -> res = value.asSequence().toString();
                        }
                    }
                    root = root.yamlMapping(elements.get(i));
                }
                return res;
            },
            () -> String.format("Attribute for key '%s' is null", key)
        );
    }
}
