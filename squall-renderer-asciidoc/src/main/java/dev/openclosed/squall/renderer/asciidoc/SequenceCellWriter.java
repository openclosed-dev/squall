package dev.openclosed.squall.renderer.asciidoc;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Sequence;

enum SequenceCellWriter implements CellWriter<Sequence> {
    TYPE_NAME("<3", SequenceAttribute.TYPE_NAME),
    START(">2", SequenceAttribute.START),
    INCREMENT(">2", SequenceAttribute.INCREMENT),
    MINIMUM(">2", SequenceAttribute.MINIMUM),
    MAXIMUM(">2", SequenceAttribute.MAXIMUM);

    private final String specifier;
    private final SequenceAttribute attribute;

    SequenceCellWriter(String specifier, SequenceAttribute attribute) {
        this.specifier = specifier;
        this.attribute = attribute;
    }

    @Override
    public final String specifier() {
        return this.specifier;
    }

    @Override
    public void writeValue(Sequence sequence, int rowNo, Appender appender, RenderContext context) {
        appender.append(this.attribute.extractValue(sequence));
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
