package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label listIndex;
    @FXML
    private Label employeeId;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label gitHubUsername;
    @FXML
    private Label salary;
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView phoneIcon;
    @FXML
    private ImageView addrIcon;
    @FXML
    private ImageView emailIcon;
    @FXML
    private ImageView gitHubIcon;
    @FXML
    private ImageView salaryIcon;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        listIndex.setText(displayedIndex + ". ");
        name.setText(person.name().fullName());
        employeeId.setText(person.id());
        phone.setText(person.phone().value());
        address.setText(person.address().value());
        gitHubUsername.setText(person.gitHubUsername().value());
        email.setText(person.email().value());
        salary.setText(String.valueOf(person.salary()));
        phoneIcon.setImage(new Image(getClass().getResourceAsStream("/images/phone_icon.png")));
        addrIcon.setImage(new Image(getClass().getResourceAsStream("/images/addr_icon.png")));
        emailIcon.setImage(new Image(getClass().getResourceAsStream("/images/email_icon.png")));
        gitHubIcon.setImage(new Image(getClass().getResourceAsStream("/images/github_icon.png")));
        salaryIcon.setImage(new Image(getClass().getResourceAsStream("/images/salary_icon.png")));
        person.tags().stream()
              .sorted(Comparator.comparing(tag -> tag.tagName))
              .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
