package dev.openclosed.squall.renderer.asciidoc;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Sequence;

enum SequenceAttributeWriter implements AttributeWriter<Sequence> {
    TYPE_NAME("<3", SequenceAttribute.TYPE_NAME),
    START(">2", SequenceAttribute.START),
    INCREMENT(">2", SequenceAttribute.INCREMENT),
    MINIMUM(">2", SequenceAttribute.MINIMUM),
    MAXIMUM(">2", SequenceAttribute.MAXIMUM);

    private final String specifier;
    private final SequenceAttribute attribute;

    SequenceAttributeWriter(String specifier, SequenceAttribute attribute) {
        this.specifier = specifier;
        this.attribute = attribute;
    }

    @Override
    public final String specifier() {
        return this.specifier;
    }

    @Override
    public void writeValue(Sequence sequence, int rowNo, DocBuilder builder, WriterContext context) {
        builder.append(this.attribute.extractValue(sequence));
    }

    static SequenceAttributeWriter writing(SequenceAttribute attribute) {
        return switch (attribute) {
            case TYPE_NAME -> TYPE_NAME;
            case START -> START;
            case INCREMENT -> INCREMENT;
            case MINIMUM -> MINIMUM;
            case MAXIMUM -> MAXIMUM;
        };
    }
}
