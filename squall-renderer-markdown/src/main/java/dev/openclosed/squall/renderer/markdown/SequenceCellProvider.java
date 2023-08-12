package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.api.spec.SpecVisitor;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

enum SequenceCellProvider implements CellProvider<Sequence> {
    TYPE_NAME(ALIGN_LEFT, Sequence::typeName),
    START(ALIGN_RIGHT, seq -> String.valueOf(seq.start())),
    INCREMENT(ALIGN_RIGHT, seq -> String.valueOf(seq.increment())),
    MINIMUM(ALIGN_RIGHT, seq -> String.valueOf(seq.minValue())),
    MAXIMUM(ALIGN_RIGHT, seq -> String.valueOf(seq.maxValue()));

    private final String separator;
    private final Function<Sequence, String> valueMapper;

    SequenceCellProvider(String separator, Function<Sequence, String> valueMapper) {
        this.separator = separator;
        this.valueMapper = valueMapper;
    }

    @Override
    public String getSeparator() {
        return this.separator;
    }

    @Override
    public String getValue(Sequence sequence, int ordinal, SpecVisitor.Context context) {
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

    static MarkdownTableWriter<Sequence> tableWriter(ResourceBundle bundle, List<SequenceAttribute> attributes) {
        List<SequenceCellProvider> providers = attributes.stream()
            .map(SequenceCellProvider::provider).toList();
        return MarkdownTableWriter.withProviders(providers, bundle);
    }
}
