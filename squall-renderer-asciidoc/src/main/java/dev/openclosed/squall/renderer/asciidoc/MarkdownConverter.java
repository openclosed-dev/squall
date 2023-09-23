/*
 * Copyright 2023 The Squall Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.openclosed.squall.renderer.asciidoc;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.Emphasis;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Heading;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Link;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.node.ThematicBreak;
import org.commonmark.parser.Parser;

import java.util.regex.Pattern;

/**
 * A converter that converts Markdown to AsciiDoc.
 */
final class MarkdownConverter extends AbstractVisitor {

    private final BaseDocBuilder builder;
    private final Parser parser;
    private boolean firstBlock;
    private int listLevel;

    private String listItemMarker;

    private static final String[] ORDERED_LIST_ITEM_MARKER = {
        ".",
        "..",
        "...",
        "....",
        ".....",
        "......",
        ".......",
        "........",
        "........."
    };

    private static final String[] UNORDERED_LIST_ITEM_MARKER = {
        "*",
        "**",
        "***",
        "****",
        "*****",
        "******",
        "*******",
        "********",
        "*********"
    };

    private static final Pattern HTML_BREAK = Pattern.compile("<br\\s*/?>");

    MarkdownConverter(BaseDocBuilder builder) {
        this.builder = builder;
        this.parser = Parser.builder().build();
    }

    void writeText(CharSequence csq) {
        this.firstBlock = true;
        this.listLevel = 0;

        Node node = this.parser.parse(csq.toString());
        node.accept(this);
    }

    // org.commonmark.node.Visitor

    @Override
    public void visit(BlockQuote blockQuote) {
        appendNewLineIfNeeded(blockQuote);
        builder.append("____").appendNewLine();
        visitChildren(blockQuote);
        builder.append("____").appendNewLine();
    }

    @Override
    public void visit(BulletList bulletList) {
        if (this.listLevel >= UNORDERED_LIST_ITEM_MARKER.length) {
            return;
        }
        if (this.listLevel == 0) {
            appendNewLineIfNeeded(bulletList);
        }
        String savedListItemMarker = this.listItemMarker;
        this.listItemMarker = UNORDERED_LIST_ITEM_MARKER[this.listLevel++];
        visitChildren(bulletList);
        this.listItemMarker = savedListItemMarker;
        this.listLevel--;
    }

    @Override
    public void visit(Code code) {
        builder.appendCode(code.getLiteral());
    }

    @Override
    public void visit(Emphasis emphasis) {
        builder.append("__");
        visitChildren(emphasis);
        builder.append("__");
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        appendNewLineIfNeeded(fencedCodeBlock);
        String info = fencedCodeBlock.getInfo();
        if (info != null && !info.isEmpty()) {
            builder.append("[source,").append(info).append(']').appendNewLine();
        }
        builder.append("----").appendNewLine();
        builder.append(fencedCodeBlock.getLiteral());
        builder.append("----").appendNewLine();
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        builder.appendHardLineBreak();
    }

    @Override
    public void visit(Heading heading) {
        appendNewLineIfNeeded(heading);
        builder.appendSectionMarker(heading.getLevel());
        if (heading.getFirstChild() != null) {
            builder.appendSpace();
        }
        visitChildren(heading);
        builder.appendNewLine();
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        appendNewLineIfNeeded(thematicBreak);
        builder.append("'''").appendNewLine();
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        var matcher = HTML_BREAK.matcher(htmlInline.getLiteral().toLowerCase());
        if (matcher.matches()) {
            builder.appendHardLineBreak();
        }
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
    }

    @Override
    public void visit(Image image) {
        builder.append("image:").append(image.getDestination()).append('[');
        visitChildren(image);
        builder.append(']');
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        appendNewLineIfNeeded(indentedCodeBlock);
        builder.append("----").appendNewLine();
        builder.append(indentedCodeBlock.getLiteral());
        builder.append("----").appendNewLine();
    }

    @Override
    public void visit(Link link) {
        builder.append(link.getDestination()).append('[');
        visitChildren(link);
        builder.append(']');
        // new line is not needed here.
    }

    @Override
    public void visit(ListItem listItem) {
        builder.append(this.listItemMarker).appendSpace();
        visitChildren(listItem);
    }

    @Override
    public void visit(OrderedList orderedList) {
        if (this.listLevel >= ORDERED_LIST_ITEM_MARKER.length) {
            return;
        }
        if (this.listLevel == 0) {
            appendNewLineIfNeeded(orderedList);
        }
        String savedListItemMarker = this.listItemMarker;
        this.listItemMarker = ORDERED_LIST_ITEM_MARKER[this.listLevel++];
        visitChildren(orderedList);
        this.listItemMarker = savedListItemMarker;
        this.listLevel--;
    }

    @Override
    public void visit(Paragraph paragraph) {
        appendNewLineIfNeeded(paragraph);
        visitChildren(paragraph);
        builder.appendNewLine();
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        builder.append("**");
        visitChildren(strongEmphasis);
        builder.append("**");
    }

    @Override
    public void visit(Text text) {
        builder.append(text.getLiteral());
    }

    @Override
    public void visit(SoftLineBreak softLineBreak) {
    }

    private void appendNewLineIfNeeded(Node node) {
        if (node.getPrevious() != null) {
            builder.appendNewLine();
        }
    }
}
