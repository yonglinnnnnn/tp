package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * A UI for the details of the organization on the left side of the screen.
 */
public class OrganizationPanel extends UiPart<Region> {

    private static final String FXML = "OrganizationPanel.fxml";

    @FXML
    private Text organizationDetails;

    @FXML
    private Text title;

    /**
     * Creates a {@code OrganizationPanel} with the given details.
     * @param organizationHierarchyString the details of the organization in Linux tree format.
     */
    public OrganizationPanel(String organizationHierarchyString) {
        super(FXML);
        organizationDetails.setText(organizationHierarchyString);
    }
}
