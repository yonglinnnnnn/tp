package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("-name");
    public static final Prefix PREFIX_PHONE = new Prefix("-hp");
    public static final Prefix PREFIX_EMAIL = new Prefix("-em");
    public static final Prefix PREFIX_ADDRESS = new Prefix("-addr");
    public static final Prefix PREFIX_TAG = new Prefix("-tag");
    public static final Prefix PREFIX_GITHUB = new Prefix("-gh");
    public static final Prefix PREFIX_TEAM = new Prefix("-team");
}
