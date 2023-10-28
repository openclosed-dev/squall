package dev.openclosed.squall.api.renderer;

import dev.openclosed.squall.api.sql.spec.Sequence;

import java.util.List;

/**
 * Attributes of a sequence.
 */
public enum SequenceAttribute {
    /** Name of the data type . */
    TYPE_NAME {
        @Override
        public String extractValue(Sequence sequence) {
            return sequence.typeName();
        }
    },
    /** Starting value for a sequence. */
    START {
        @Override
        public String extractValue(Sequence sequence) {
            return String.valueOf(sequence.start());
        }
    },
    /** Specifies which value is added to the current sequence value to create a new value.  */
    INCREMENT {
        @Override
        public String extractValue(Sequence sequence) {
            return String.valueOf(sequence.increment());
        }
    },
    /** Minimum value for a sequence. */
    MINIMUM {
        @Override
        public String extractValue(Sequence sequence) {
            return String.valueOf(sequence.minValue());
        }
    },
    /** Maximum value for a sequence. */
    MAXIMUM {
        @Override
        public String extractValue(Sequence sequence) {
            return String.valueOf(sequence.maxValue());
        }
    };

    private static final List<SequenceAttribute> DEFAULT_LIST = List.of(
        TYPE_NAME,
        START,
        INCREMENT,
        MINIMUM,
        MAXIMUM
    );

    /**
     * Extracts a value from the specified sequence.
     * @param sequence the sequence that provides the value.
     * @return the extracted value as a string.
     */
    public abstract String extractValue(Sequence sequence);

    /**
     * Returns the default list of the attributes.
     * @return the default list of the attributes.
     */
    public static List<SequenceAttribute> defaultList() {
        return DEFAULT_LIST;
    }
}
