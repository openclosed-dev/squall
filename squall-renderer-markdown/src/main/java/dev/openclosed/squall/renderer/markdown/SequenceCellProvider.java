package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Sequence;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

enum SequenceCellProvider implements CellProvider<Sequence, Void> {
    DATA_TYPE(ALIGN_LEFT, Sequence::typeName),
    START(ALIGN_RIGHT, seq -> String.valueOf(seq.start())),
    INCREMENT(ALIGN_RIGHT, seq -> String.valueOf(seq.increment())),
    MIN_VALUE(ALIGN_RIGHT, seq -> String.valueOf(seq.minValue())),
    MAX_VALUE(ALIGN_RIGHT, seq -> String.valueOf(seq.maxValue()));

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
    public String getValue(Sequence sequence, Void unused, int ordinal) {
        return valueMapper.apply(sequence);
    }

    static SequenceCellProvider provider(SequenceAttribute attribute) {
        return switch (attribute) {
            case DATA_TYPE -> DATA_TYPE;
            case START -> START;
            case INCREMENT -> INCREMENT;
            case MIN_VALUE -> MIN_VALUE;
            case MAX_VALUE -> MAX_VALUE;
        };
    }

    static MarkdownTableWriter<Sequence, Void> tableWriter(ResourceBundle bundle, List<SequenceAttribute> attributes) {
        List<SequenceCellProvider> providers = attributes.stream()
            .map(SequenceCellProvider::provider).toList();
        return MarkdownTableWriter.withProviders(providers, bundle);
    }
}
