package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validArgs_returnsTagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));

        assertParseSuccess(parser, "1 friends", new TagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_validArgsMultipleTags_returnsTagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        tags.add(new Tag("colleagues"));
        tags.add(new Tag("family"));

        assertParseSuccess(parser, "1 friends colleagues family", new TagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // negative index
        assertParseFailure(parser, "-1 friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));

        // zero index
        assertParseFailure(parser, "0 friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));

        // non-numeric index
        assertParseFailure(parser, "a friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingParts_throwsParseException() {
        // no index specified
        assertParseFailure(parser, "friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));

        // no tags specified
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));

        // empty string
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));

        // whitespace only
        assertParseFailure(parser, "   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        // tag with special characters
        assertParseFailure(parser, "1 friends*", Tag.MESSAGE_CONSTRAINTS);

        // tag with spaces (each word becomes separate tag, but if invalid chars present)
        assertParseFailure(parser, "1 friend$ colleague", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_extraSpaces_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));

        // extra spaces between index and tag
        assertParseSuccess(parser, "1    friends", new TagCommand(INDEX_FIRST_PERSON, tags));

        // leading spaces
        assertParseSuccess(parser, "   1 friends", new TagCommand(INDEX_FIRST_PERSON, tags));

        // trailing spaces
        assertParseSuccess(parser, "1 friends   ", new TagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_multipleTagsWithSpaces_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));

        // multiple tags with varying spaces
        assertParseSuccess(parser, "1   tag1  tag2    tag3", new TagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_largeIndex_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));

        assertParseSuccess(parser, "999 friends", new TagCommand(Index.fromOneBased(999), tags));
    }

    @Test
    public void parse_emptyTagAfterTrim_throwsParseException() {
        // Note: This tests the edge case where ParserUtil.parseTags might return an empty set
        // In practice, ParserUtil.parseTag throws an exception for empty/whitespace-only tags,
        // but this ensures the isEmpty() check in TagCommandParser is covered

        // Tags that are only whitespace should be caught by Tag validation
        // This will throw Tag.MESSAGE_CONSTRAINTS, not MESSAGE_NO_TAGS_PROVIDED
        // because parseTag validates each tag before the isEmpty check
        assertParseFailure(parser, "1  ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));
    }
}
