package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * A UI for the details of the organization on the left side of the screen.
 */
public class OrganizationPanel extends UiPart<Region> {

    private static final String FXML = "OrganizationPanel.fxml";

    @FXML
    private Label organizationDetails;

    @FXML
    private Text title;

    /**
     * Creates a {@code OrganizationPanel} with the given details.
     */
    public OrganizationPanel() {
        super(FXML);
    }

    /**
     * Sets the details of the organization to be displayed.
     * @param details the details of the organization
     */
    public void setHierarchyDisplay(String details) {
        requireNonNull(details);
        organizationDetails.setText(details);
    }

}
