package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UntagCommand;
import seedu.address.model.tag.Tag;

public class UntagCommandParserTest {

    private UntagCommandParser parser = new UntagCommandParser();

    @Test
    public void parse_validArgs_returnsUntagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));

        assertParseSuccess(parser, "E1001 friends", new UntagCommand("E1001", tags));
    }

    @Test
    public void parse_validArgsMultipleTags_returnsUntagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        tags.add(new Tag("colleagues"));
        tags.add(new Tag("family"));

        assertParseSuccess(parser, "E1001 friends colleagues family", new UntagCommand("E1001", tags));
    }

    @Test
    public void parse_invalidEmployeeId_throwsParseException() {
        // doesn't start with E
        assertParseFailure(parser, "1001 friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));

        // starts with lowercase e
        assertParseFailure(parser, "e1001 friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));

        // random string
        assertParseFailure(parser, "ABC friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingParts_throwsParseException() {
        // no employee ID specified
        assertParseFailure(parser, "friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));

        // no tags specified
        assertParseFailure(parser, "E1001", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));

        // empty string
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));

        // whitespace only
        assertParseFailure(parser, "   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        // tag with special characters
        assertParseFailure(parser, "E1001 friends*", Tag.MESSAGE_CONSTRAINTS);

        // tag with invalid characters
        assertParseFailure(parser, "E1001 friend$ colleague", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_extraSpaces_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));

        // extra spaces between employee ID and tag
        assertParseSuccess(parser, "E1001    friends", new UntagCommand("E1001", tags));

        // leading spaces
        assertParseSuccess(parser, "   E1001 friends", new UntagCommand("E1001", tags));

        // trailing spaces
        assertParseSuccess(parser, "E1001 friends   ", new UntagCommand("E1001", tags));
    }

    @Test
    public void parse_multipleTagsWithSpaces_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));

        // multiple tags with varying spaces
        assertParseSuccess(parser, "E1001   tag1  tag2    tag3", new UntagCommand("E1001", tags));
    }

    @Test
    public void parse_differentEmployeeIds_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));

        assertParseSuccess(parser, "E1 friends", new UntagCommand("E1", tags));
        assertParseSuccess(parser, "E9999 friends", new UntagCommand("E9999", tags));
        assertParseSuccess(parser, "E123456 friends", new UntagCommand("E123456", tags));
    }

    @Test
    public void parse_emptyTagAfterTrim_throwsParseException() {
        assertParseFailure(parser, "E1001  ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));
    }
}
