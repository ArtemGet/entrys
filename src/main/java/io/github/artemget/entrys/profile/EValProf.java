package io.github.artemget.entrys.profile;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import io.github.artemget.entrys.ESafe;
import io.github.artemget.entrys.Entry;
import io.github.artemget.entrys.EntryException;
import io.github.artemget.entrys.file.EFile;
import io.github.artemget.entrys.file.EVal;
import io.github.artemget.entrys.operation.EContains;
import io.github.artemget.entrys.operation.EFork;
import io.github.artemget.entrys.operation.ESplit;
import io.github.artemget.entrys.operation.EUnwrap;
import io.github.artemget.entrys.system.EEnv;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class EValProf extends ESafe<String> {

    public EValProf(final String key) {
        this(key, "src/main/resources/application.yaml");
    }

    public EValProf(final String key, final String path) {
        this(key, new EFile(path), path);
    }

    public EValProf(final String key, final Entry<String> content, final String path) {
        super(
                () -> {
                    YamlMapping root;
                    try {
                        root = Yaml.createYamlInput(content.value())
                                .readYamlMapping();
                    } catch (IOException | EntryException exception) {
                        throw new EntryException(
                                String.format("Failed to read yaml mapping for key: '%s'", key),
                                exception
                        );
                    }
                    String res = null;
                    YamlNode profileNode = getProfileNode(root);
                    if (profileNode != null) {
                        String profile = EValProf.ejected(profileNode);
                        res = new EVal(key, parsePath(path, profile)).value();
                    } else {
                        res = new EVal(key, content).value();
                    }
                    return res;
                },
                () -> String.format("Attribute for key '%s' is null", key)
        );
    }

    private static YamlNode getProfileNode(final YamlMapping root) {
        final List<String> path = Arrays.asList("entrys","profile");
        YamlNode currentNode = root;
        for(String part: path) {
            if (currentNode instanceof YamlMapping) {
                currentNode = ((YamlMapping) currentNode).value(part);
            } else {
                return null;
            }
        }
        return currentNode;
    }

    private static String ejected(final YamlNode profileNode) throws EntryException {
        final String scalar = profileNode.asScalar().value();

        return new EFork<>(
                new EContains(scalar::isBlank),
                () -> {
                    throw new EntryException("Attribute for key 'profile' is empty");
                },
                () -> new EFork<>(
                        () -> scalar.startsWith("${") && scalar.endsWith("}"),
                        new EFork<>(
                                () -> new ESplit(() -> scalar, ":").value().size() >= 2,
                                new EFork<>(
                                        new EContains(new EEnv(() -> EValProf.selectedEnv(scalar).trim())),
                                        new EEnv(() -> EValProf.selectedEnv(scalar).trim()),
                                        () -> EValProf.joined(scalar)
                                ),
                                new EEnv(new EUnwrap(scalar, "${", "}"))
                        ),
                        () -> scalar
                ).value()
        ).value();
    }

    private static String parsePath(final String path, final String profile) {
        return path.replace("application", "application-" + profile);
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