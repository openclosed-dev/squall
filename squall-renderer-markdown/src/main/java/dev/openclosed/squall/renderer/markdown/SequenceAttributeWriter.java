package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Sequence;

enum SequenceAttributeWriter implements AttributeWriter<Sequence> {
    TYPE_NAME(ALIGN_LEFT, SequenceAttribute.TYPE_NAME),
    START(ALIGN_RIGHT, SequenceAttribute.START),
    INCREMENT(ALIGN_RIGHT, SequenceAttribute.INCREMENT),
    MINIMUM(ALIGN_RIGHT, SequenceAttribute.MINIMUM),
    MAXIMUM(ALIGN_RIGHT, SequenceAttribute.MAXIMUM);

    private final String separator;
    private final SequenceAttribute attribute;

    SequenceAttributeWriter(String separator, SequenceAttribute attribute) {
        this.separator = separator;
        this.attribute = attribute;
    }

    @Override
    public String getSeparator() {
        return this.separator;
    }

    @Override
    public void writeValue(Sequence sequence, int ordinal, DocBuilder builder, WriterContext context) {
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
