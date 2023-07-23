package dev.openclosed.squall.api.renderer;

import java.util.List;

/**
 * Attributes of a sequence.
 */
public enum SequenceAttribute {
    DATA_TYPE,
    START,
    INCREMENT,
    MIN_VALUE,
    MAX_VALUE;

    private static final List<SequenceAttribute> DEFAULT_LIST = List.of(
        DATA_TYPE,
        START,
        INCREMENT,
        MIN_VALUE,
        MAX_VALUE
    );

    /**
     * Returns the default list of the attributes.
     * @return the default list of the attributes.
     */
    public static List<SequenceAttribute> defaultList() {
        return DEFAULT_LIST;
    }
}
