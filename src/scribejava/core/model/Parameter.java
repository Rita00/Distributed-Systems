package scribejava.core.model;

import com.github.scribejava.core.utils.OAuthEncoder;

/**
 * @author Pablo Fernandez
 */
public class Parameter implements Comparable<com.github.scribejava.core.model.Parameter> {

    private static final String UTF = "UTF8";

    private final String key;
    private final String value;

    public Parameter(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String asUrlEncodedPair() {
        return OAuthEncoder.encode(key).concat("=").concat(OAuthEncoder.encode(value));
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof com.github.scribejava.core.model.Parameter)) {
            return false;
        }

        final com.github.scribejava.core.model.Parameter otherParam = (com.github.scribejava.core.model.Parameter) other;
        return otherParam.getKey().equals(key) && otherParam.getValue().equals(value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }

    @Override
    public int compareTo(final com.github.scribejava.core.model.Parameter parameter) {
        final int keyDiff = key.compareTo(parameter.getKey());

        return keyDiff == 0 ? value.compareTo(parameter.getValue()) : keyDiff;
    }
}
