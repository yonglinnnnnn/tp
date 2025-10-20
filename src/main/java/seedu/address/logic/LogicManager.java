package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AuditCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(commandText);
        commandResult = command.execute(model);

        // Only log commands that modify state
        if (shouldLogCommand(command)) {
            String action = extractAction(command);
            String details = generateDetails(command, commandResult);
            model.getAuditLog().addEntry(action, details, LocalDateTime.now());
        }

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    /**
     * Extracts the action type from the given command.
     *
     * @param command The command executed.
     * @return The action type as a string.
     */
    private String extractAction(Command command) {
        // Extract command type from class name
        String className = command.getClass().getSimpleName();
        return className.replace("Command", "").toUpperCase();
    }

    /**
     * Generates meaningful details for the audit log based on the command and its result.
     * @param command   The command executed
     * @param result    The result of the command execution
     * @return  String of details for the audit log
     */
    private String generateDetails(Command command, CommandResult result) {
        if (command instanceof AuditCommand) {
            return "Viewed audit log.";
        }
        return result.getFeedbackToUser();
    }

    /**
     * Determines if a command should be logged in the audit log.
     * Only commands that modify state are logged.
     *
     * @param command The command to check.
     * @return true if the command should be logged, false otherwise.
     */
    private boolean shouldLogCommand(Command command) {
        String action = extractAction(command);
        // Exclude read-only commands
        return !action.equals("AUDIT")
                && !action.equals("EXIT")
                && !action.equals("HELP")
                && !action.equals("LIST")
                && !action.equals("VIEW")
                && !action.equals("FIND"); // Add other read-only commands as needed
    }
}
