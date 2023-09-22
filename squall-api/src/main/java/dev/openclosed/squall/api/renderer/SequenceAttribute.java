package dev.openclosed.squall.api.renderer;

import dev.openclosed.squall.api.spec.Sequence;

import java.util.List;

/**
 * Attributes of a sequence.
 */
public enum SequenceAttribute {
    TYPE_NAME {
        @Override
        public String extractValue(Sequence sequence) {
            return sequence.typeName();
        }
    },
    START {
        @Override
        public String extractValue(Sequence sequence) {
            return String.valueOf(sequence.start());
        }
    },
    INCREMENT {
        @Override
        public String extractValue(Sequence sequence) {
            return String.valueOf(sequence.increment());
        }
    },
    MINIMUM {
        @Override
        public String extractValue(Sequence sequence) {
            return String.valueOf(sequence.minValue());
        }
    },
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

    public abstract String extractValue(Sequence sequence);

    /**
     * Returns the default list of the attributes.
     * @return the default list of the attributes.
     */
    public static List<SequenceAttribute> defaultList() {
        return DEFAULT_LIST;
    }
}
