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

package io.github.artemget.entrys.json;

import io.github.artemget.entrys.EntryException;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link io.github.artemget.entrys.json.EJsonObj}.
 *
 * @since 0.0.1
 */
final class EJsonObjTest {

    @Test
    void throwsAtWrongAttrType() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EJsonObj(
                Json.createObjectBuilder()
                    .add("name", "not json object")
                    .build(),
                "name"
            ).value(),
            "EJsonObj did not rethrow at wrong field type"
        );
    }

    @Test
    void throwsAtNullValue() {
        Assertions.assertThrows(
            EntryException.class,
            () -> new EJsonObj(
                Json.createObjectBuilder().build(),
                "name"
            ).value(),
            "EJsonObj did not rethrow at null"
        );
    }

    @Test
    void returnsObject() throws EntryException {
        final JsonObject object = Json.createObjectBuilder()
            .add(
                "user",
                Json.createObjectBuilder()
                    .add("name", "zarif")
                    .build()
            ).build();
        MatcherAssert.assertThat(
            "EJsonObj did not return expected value",
            new EJsonObj(
                object,
                "user"
            ).value(),
            Matchers.equalTo(object.getJsonObject("user"))
        );
    }
}
