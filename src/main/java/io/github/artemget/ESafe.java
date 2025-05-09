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

package io.github.artemget;

/**
 * Null safe entry. Throws if wrapped
 * scalar return null.
 * @param <T> Type
 * @since 0.0.1
 * @checkstyle DesignForExtensionCheck (70 lines)
 */
public class ESafe<T> implements Entry<T> {
    /**
     * Scalar returning T.
     */
    private final Entry<T> origin;

    /**
     * Scalar returning error message if origin returned null or thrown NPE.
     */
    private final Entry<String> message;

    /**
     * Main ctor.
     * @param origin Scalar
     */
    public ESafe(final Entry<T> origin) {
        this(origin, () -> "Empty entry");
    }

    /**
     * Main ctor.
     * @param origin Scalar
     * @param message Thrown if null
     */
    public ESafe(final Entry<T> origin, final Entry<String> message) {
        this.origin = origin;
        this.message = message;
    }

    @SuppressWarnings({"PMD.AvoidCatchingNPE", "PMD.AvoidCatchingGenericException"})
    @Override
    public T value() throws EntryException {
        final T value;
        try {
            value = this.origin.value();
        } catch (final NullPointerException exception) {
            throw new EntryException(this.message.value(), exception);
        }
        if (value == null) {
            throw new EntryException(this.message.value());
        }
        return value;
    }
}
