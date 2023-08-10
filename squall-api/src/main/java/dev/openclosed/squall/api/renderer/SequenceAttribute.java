package dev.openclosed.squall.api.renderer;

import java.util.List;

/**
 * Attributes of a sequence.
 */
public enum SequenceAttribute {
    TYPE_NAME,
    START,
    INCREMENT,
    MINIMUM,
    MAXIMUM;

    private static final List<SequenceAttribute> DEFAULT_LIST = List.of(
        TYPE_NAME,
        START,
        INCREMENT,
        MINIMUM,
        MAXIMUM
    );

    /**
     * Returns the default list of the attributes.
     * @return the default list of the attributes.
     */
    public static List<SequenceAttribute> defaultList() {
        return DEFAULT_LIST;
    }
}
