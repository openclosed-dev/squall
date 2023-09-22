package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Sequence;

import java.util.function.Function;

enum SequenceCellWriter implements CellWriter<Sequence> {
    TYPE_NAME(ALIGN_LEFT, Sequence::typeName),
    START(ALIGN_RIGHT, seq -> String.valueOf(seq.start())),
    INCREMENT(ALIGN_RIGHT, seq -> String.valueOf(seq.increment())),
    MINIMUM(ALIGN_RIGHT, seq -> String.valueOf(seq.minValue())),
    MAXIMUM(ALIGN_RIGHT, seq -> String.valueOf(seq.maxValue()));

    private final String separator;
    private final Function<Sequence, String> valueMapper;

    SequenceCellWriter(String separator, Function<Sequence, String> valueMapper) {
        this.separator = separator;
        this.valueMapper = valueMapper;
    }

    @Override
    public String getSeparator() {
        return this.separator;
    }

    @Override
    public void writeValue(Sequence sequence, int ordinal, Appender appender, RenderContext context) {
        appender.append(valueMapper.apply(sequence));
    }

    static SequenceCellWriter writing(SequenceAttribute attribute) {
        return switch (attribute) {
            case TYPE_NAME -> TYPE_NAME;
            case START -> START;
            case INCREMENT -> INCREMENT;
            case MINIMUM -> MINIMUM;
            case MAXIMUM -> MAXIMUM;
        };
    }
}
