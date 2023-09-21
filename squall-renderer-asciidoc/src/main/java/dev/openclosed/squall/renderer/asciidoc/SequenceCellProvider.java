package dev.openclosed.squall.renderer.asciidoc;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Sequence;

import java.util.function.Function;

enum SequenceCellProvider implements CellProvider<Sequence> {
    TYPE_NAME("<3", Sequence::typeName),
    START(">2", seq -> String.valueOf(seq.start())),
    INCREMENT(">2", seq -> String.valueOf(seq.increment())),
    MINIMUM(">2", seq -> String.valueOf(seq.minValue())),
    MAXIMUM(">2", seq -> String.valueOf(seq.maxValue()));

    private final String specifier;
    private final Function<Sequence, String> valueMapper;

    SequenceCellProvider(String specifier, Function<Sequence, String> valueMapper) {
        this.specifier = specifier;
        this.valueMapper = valueMapper;
    }

    @Override
    public final String specifier() {
        return this.specifier;
    }

    @Override
    public final String getValue(Sequence sequence, int ordinal, RenderContext context) {
        return valueMapper.apply(sequence);
    }

    static SequenceCellProvider provider(SequenceAttribute attribute) {
        return switch (attribute) {
            case TYPE_NAME -> TYPE_NAME;
            case START -> START;
            case INCREMENT -> INCREMENT;
            case MINIMUM -> MINIMUM;
            case MAXIMUM -> MAXIMUM;
        };
    }
}
