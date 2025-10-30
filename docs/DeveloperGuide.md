---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Henri Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, `OrganizationPanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/resources/view/MainWindow.fxml)
The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103-F12-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Import feature enhancement

{To explain how import feature is to be added}

### \[\] Customisable command words to fit user preference
{To explain how command word will be customisable}


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of employee contacts in a tech company
* has a need to track team structures and developer allocations
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Our application helps HR administrators who want to efficiently manage contact information, track team structures and developer allocations. It will provide ease of access and increased efficiency with assistive tools for keyboard-based users that will speed up the storage and retrieval of HR data.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                    | I want to …​                                                                      | So that I can…​                                                        |
|----------|----------------------------|-----------------------------------------------------------------------------------|------------------------------------------------------------------------|
| `* * *`  | HR administrator           | add people’s contact to my address book                                           | add them when they join the company                                    |
| `* * *`  | HR administrator           | delete people’s contact to my address book                                        | remove them when they leave the company                                |
| `* * *`  | HR administrator           | edit people’s contact in my address book                                          | update their information                                               |
| `* * *`  | HR administrator           | directly export my saved contacts                                                 | save them as a backup                                                  |
| `* * *`  | HR administrator           | create custom tags for employee roles                                             | easily identify contacts based on their job function                   |
| `* * *`  | HR administrator           | remove custom tags for employee roles                                             | remove irrelevant tags from the employee                               |
| `* * *`  | HR administrator           | see employee details including salary                                             | find out more details about them                                       |
| `* * *`  | HR administrator           | view feedback when actions are completed                                          | can confirm my actions were properly executed                          |
| `* * *`  | HR administrator           | use contact management apps based on the command line                             | appear tech-savvy in my work style                                     |
| `* * *`  | HR administrator           | update contact information easily                                                 | keep employees' data up to date                                        |
| `* * *`  | HR administrator           | create team                                                                       | organise employees into d teams                                        |
| `* * *`  | HR administrator           | delete team                                                                       | delete a useless team that is no longer used                           |
| `* * *`  | HR administrator           | add employee to team                                                              | add an employee to their respective team                               |
| `* * *`  | HR administrator           | remove employee from team                                                         | keep the list of team members updated                                  |
| `* * *`  | HR administrator           | view audit log of past actions                                                    | keep track of the actions that change the details of the address book. |
| `* * *`  | HR administrator           | search for specific employee                                                      | to get the specific employee details                                   |
| `* * *`  | HR administrator           | update contact information easily                                                 | keep employees' data up to date                                        |
| `* * *`  | forgetful HR administrator | have some way to see available commands                                           | recall what are the commands I forgot                                  |
| `* * *`  | HR administrator           | clean the address book                                                            | reset it to clean state                                                |
| `* * *`  | HR administrator           | update contact information easily                                                 | keep employees' data up to date                                        |
| `* * *`  | HR administrator           | update contact information easily                                                 | keep employees' data up to date                                        |
| `* *`    | HR administrator           | import users from another address book                                            | convert from one address book to another easily                        |
| `* *`    | HR administrator           | sort the employee based on specific fields                                        | see the specifc information I want near the top                        |
| `* *`    | HR administrator           | set sub teams to teams                                                            | to indicate the sub departments                                        |
| `* * `   | HR administrator           | see a company-level overview of all the employees and the departments they are in | have a better idea of the manpower allocation.                         |

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a person**

**MSS**

1. User requests to add a new person with specified details (name, phone, email, GitHub username, etc.)
2. System validates the input 
3. System adds the person to the address book 
4. System displays a success message with the added person's details
   Use case ends.

**Extensions:**
* 2a. Required fields are missing.
  * 2a1. System shows an error message indicating which fields are required.
    Use case ends.

* 2b. The email format is invalid. 
  * 2b1. System shows an error message about the invalid email format.
    Use case ends.

* 2c. The GitHub username already exists. 
  * 2c1. System shows an error message indicating duplicate GitHub username.
    Use case ends.

* 2d. The phone number format is invalid. 
  * 2d1. System shows an error message about the invalid phone format.
    Use case ends.

**Use case: Add employee to team**

**MSS**
1. User requests to add a person to a team by specifying employee ID and team name 
2. System validates that both the person and team exist 
3. System adds the person to the specified team 
4. System displays a success message
   Use case ends.

**Extensions**
* 2a. The employee ID does not exist.
  * 2a1. System shows an error message.
    Use case ends. 
* 2b. The team name does not exist. 
  * 2b1. System shows an error message.
    Use case ends. 
* 2c. The person is already in the team. 
  * 2c1. System shows an error message indicating the person is already a team member.
    Use case ends.

**Use case: View audit log**

**MSS**
1. User requests to view the audit log 
2. System displays a list of all recorded actions with timestamps, user, and details of changes made 
3. User reviews the audit history
   Use case ends.

**Extensions**
* 1a. No actions have been recorded yet.
  * 1a1. System shows an empty audit log or a message indicating no history is available.
    Use case ends.

**Use case: Clear address book**

**MSS** 
1. User requests to clear all data from the address book 
2. System prompts for confirmation 
3. User confirms the action 
4. System clears all persons and teams from the address book 
5. System displays a success message
   Use case ends.

**Use case: Create team**

**MSS**
1. User requests to create a new team with a specified team name 
2. System validates the team name 
3. System creates the team 
4. System displays a success message
   Use case ends.

**Extensions**
* 2a. The team name already exists. 
  * 2a1. System shows an error message indicating duplicate team name.
    Use case ends. 
* 2b. The team name format is invalid. 
  * 2b1. System shows an error message about the invalid format.
    Use case ends.

**Use case: Delete a person**

**MSS**

1.  User requests to list persons
2.  System shows a list of persons
3.  User finds the person's ID
4.  User requests to delete a specific person in the list by ID
5.  System deletes the person
    Use case ends.

**Extensions**

* 2a. The list is empty.
  Use case ends.
* 3a. The given ID is invalid.
    * 3a1. System shows an error message.
      Use case resumes at step 2.

**Use case: Delete team**

**MSS**
1. User requests to delete a team by specifying team name 
2. System validates that the team exists 
3. System removes the team and all its associations 
4. System displays a success message
   Use case ends.

**Extensions**
* 2a. The team name does not exist. 
  * 2a1. System shows an error message.
    Use case ends. 
* 2b. The team has members. 
  * 2b1. System shows a warning and asks for confirmation. 
  * 2b2. User confirms deletion. 
  * 2b3. System removes all members from the team and deletes the team.
    Use case resumes at step 4.

**Use case: Edit details of a person**

**MSS** 
1. User requests to list persons 
2. System shows a list of persons 
3. User finds the person's ID 
4. User requests to update details of a specific person in the list by ID 
5. System shows the updated details of the person
    Use case ends.

**Extensions**
* 2a. The list is empty.
  Use case ends.
* 3a. The given ID is invalid.
    * 3a1. System shows an error message.
      Use case resumes at step 2.

**Use case: Exit application**

**MSS**
1. User requests to exit the application 
2. System saves all data 
3. System closes the application
   Use case ends.

**Extensions**
* 2a. There are unsaved changes. 
  * 2a1. System saves the changes before closing.
    Use case resumes at step 3.

**Use case: Display help**

**MSS**
1. User requests help information 
2. System displays a help window with available commands and their usage 
3. User reviews the help information
   Use case ends.

**Extensions**
* 1a. User specifies a specific command to get help for. 
  * 1a1. System displays detailed help for that specific command.
    Use case ends.

**Use case: Import contacts**

**MSS**
1. User requests to import contacts from a file by specifying file path 
2. System validates the file format 
3. System reads the file and imports all valid contacts 
4. System displays a summary of imported contacts and any errors encountered
   Use case ends.

**Extensions**
* 2a. The file does not exist. 
  * 2a1. System shows an error message.
    Use case ends. 
* 2b. The file format is invalid or corrupted. 
  * 2b1. System shows an error message about the invalid format.
    Use case ends. 
* 3a. Some contacts in the file have invalid data. 
  * 3a1. System skips invalid contacts and imports valid ones. 
  * 3a2. System shows a summary of skipped contacts with reasons.
    Use case resumes at step 4. 
* 3b. Some contacts already exist (duplicate GitHub usernames). 
  * 3b1. System skips duplicate contacts. 
  * 3b2. System shows a summary of skipped duplicates.
    Use case resumes at step 4.

**Use case: List persons**

**MSS**
1. User requests to list all persons 
2. System displays all persons in the address book with their basic information
   Use case ends.

**Extensions**
* 1a. The address book is empty. 
  * 1a1. System shows a message indicating no persons found.
    Use case ends.

**Use case: Remove employee from team**

**MSS**

1. User requests to remove a person from a team by specifying employee ID and team name 
2. System validates that both the person and team exist 
3. System validates that the person is a member of the team 
4. System removes the person from the team 
5. System displays a success message
   Use case ends.

**Extensions**
* 2a. The employee ID does not exist. 
  * 2a1. System shows an error message.
    Use case ends. 
* 2b. The team name does not exist. 
  * 2b1. System shows an error message.
    Use case ends. 
* 3a. The person is not in the team. 
  * 3a1. System shows an error message indicating the person is not a team member.
    Use case ends.

**Use case: Set employee salary**

**MSS**
1. User requests to set salary for a person by specifying employee ID and salary amount 
2. System validates the employee ID exists 
3. System validates the salary format 
4. System updates the person's salary 
5. System displays a success message with the updated salary
   Use case ends.

**Extensions**
* 2a. The employee ID does not exist. 
  * 2a1. System shows an error message.
    Use case ends. 
* 3a. The salary format is invalid (negative or non-numeric). 
  * 3a1. System shows an error message about the invalid salary format.
    Use case ends.

**Use case: Set Subteam**

**MSS**
1. User requests to set a subteam relationship by specifying parent team and child team names 
2. System validates that both teams exist 
3. System validates that setting this relationship does not create a circular dependency 
4. System sets the child team as a subteam of the parent team 
5. System displays a success message
   Use case ends.

**Extensions**
* 2a. The parent team does not exist. 
  * 2a1. System shows an error message.
    Use case ends. 
* 2b. The child team does not exist. 
  * 2b1. System shows an error message.
    Use case ends. 
* 3a. Setting this relationship would create a circular dependency. 
  * 3a1. System shows an error message indicating circular dependency.
    Use case ends. 
* 3b. The child team is already a subteam of the parent team. 
  * 3b1. System shows an error message indicating the relationship already exists.
    Use case ends.

**Use case: Sort persons**

**MSS**
1. User requests to sort persons by a specified field (name, salary, etc.)
2. System validates the sort field 
3. System sorts the persons list according to the specified field 
4. System displays the sorted list
   Use case ends.

**Extensions**
* 2a. The sort field is invalid. 
  * 2a1. System shows an error message listing valid sort fields.
    Use case ends. 
* 3a. The address book is empty. 
  * 3a1. System shows a message indicating no persons to sort.
    Use case ends.


**Use case: Add tags to a person**

1.  User requests to list persons
2.  System shows a list of persons
3.  User finds the person's ID
4.  User requests to add a tag to the person by ID
5.  System adds the tag to the person

    Use case ends.

**Extensions**
* 2a. The list is empty.
  Use case ends.
* 3a. The given ID is invalid.
    * 3a1. System shows an error message.
      Use case resumes at step 2.
* 4a. The tag already exists.
    * 4a1. System shows an error message.
      Use case resumes at step 2.

**Use case: Remove tags from a person**
1.  User requests to list persons
2.  System shows a list of persons
3.  User finds the person's ID
4.  User requests to remove specific tags from the person by ID
5.  System removes the specified tags from the person (case-insensitive matching)
6.  System displays a success message listing removed tags
    Use case ends.

**Extensions**
* 2a. The list is empty.
  Use case ends.
* 3a. The given ID is invalid.
  * 3a1. System shows an error message.
    Use case resumes at step 2. 
* 5a. Some specified tags do not exist on the person. 
  * 5a1. System removes only the existing tags. 
  * 5a2. System displays a warning message listing tags that were not found.
    Use case resumes at step 6. 
* 5b. None of the specified tags exist on the person. 
  * 5b1. System shows an error message indicating no tags were removed.
    Use case ends.

**Use case: Display company overview**

1.  User requests to display company overview
2.  System shows departments, project teams and employees in a tree view

    Use case ends.

**Use case: Sort persons**

1.  User requests to sort persons
2.  User chooses the sorting criteria (e.g. by name, by department, by project team, etc.)
3.  System sorts persons by the chosen criteria
4.  System lists down persons in sorted order

    Use case ends.

**Use case: Search an employee by GitHub username**

1.  User requests to search an employee by GitHub username
2.  System shows the employee's details

    Use case ends.

**Extensions**

* 1a. The GitHub username does not exist.

  * 1a1. System shows an error message.

      Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The different elements of the user interface should be arranged according to level of impact/priority (i.e immediate feedback somewhere close to the input box/as a pop-up.
5.  The command manual should be easily understandable by a first-time user of the application.
6.  Updated contacts should not be lost despite the unexpected closing of the application.
7.  Error messages should be displayed to the user within 200 milliseconds.
8.  All error messages should be easily understandable by a non-technical user and suggest a possible solution.
9.  The save process should ensure 100% data integrity, preventing corruption even if the system crashes during the save.
10. The exported contact list file should be compatible with any mainstream OS.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Add command

1. Adding a person with all required fields

    1. Test case: `add -name John Doe -hp 98765432 -em johnd@example.com -addr Woodalnds`<br>
       Expected: New person is added to the list. Success message displays the added person's details including auto-generated employee ID.

    2. Test case: `add -name Jane Smith -hp 87654321 -em janes@example.com -addr Woodlands -gh @JaneSmithy`<br>
       Expected: New person is added with optional github field. Success message shows all details.

2. Adding a person with missing required fields

    1. Test case: `add -name John Doe -hp 98765432`<br>
       Expected: No person is added. Error message indicates missing required fields (email and address).

    2. Test case: `add`<br>
       Expected: No person is added. Error message shows the correct command format.

3. Adding a person with invalid fields

    1. Test case: `add -name John Doe -hp invalid -em johnd@example.com -gh @johndoe`<br>
       Expected: No person is added. Error message indicates invalid phone number format.

    2. Test case: `add -name John Doe -hp 98765432 -em invalidemail -gh @johndoe`<br>
       Expected: No person is added. Error message indicates invalid email format.


### Add-to-team command

1. Adding an employee to a team

    1. Prerequisites: Person with ID E1001 exists and team "T9999" exists.

    2. Test case: `add-to-team T9999 E1001`<br>
       Expected: Person E1001 is added to the T9999 team. Success message confirms the addition.

2. Adding an employee to a non-existent team

    1. Prerequisites: Person with ID E1001 exists but team "T9999" does not exist.

    2. Test case: `add-to-team T9999 E1001`<br>
       Expected: No changes made. Error message indicates the team "T9999" does not exist.

3. Adding a non-existent employee to a team

    1. Prerequisites: Team "T9999" exists but no person with ID E1001 exists.

    2. Test case: `add-to-team T9999 E1001`<br>
       Expected: No changes made. Error message indicates the employee ID "E1001" does not exist.

4. Adding an employee already in the team

    1. Prerequisites: Person E1001 is already a member of team "T9999".

    2. Test case: `add-to-team T9999 E1001`<br>
       Expected: No changes made. Error message indicates the person is already in the team.

### Audit command

1. Viewing audit log with recorded actions

    1. Prerequisites: Some actions have been performed (e.g., add, delete, edit).

    2. Test case: `audit`<br>
       Expected: Output feedback displays showing all recorded actions with timestamps and details.

2. Viewing audit log with no recorded actions

    1. Prerequisites: Fresh application start with no actions performed.

    2. Test case: `audit`<br>
       Expected: Output feedback displays with empty list or message indicating no history is available.

### Clear command

1. Clearing the address book

    1. Prerequisites: Multiple persons and teams exist in the address book.

    2. Test case: `clear`<br>
       Expected: All persons and teams are removed from the address book. Success message confirms the address book has been cleared.

2. Clearing an already empty address book

    1. Prerequisites: Address book is empty.

    2. Test case: `clear`<br>
       Expected: Success message indicates the address book has been cleared (even though it was already empty).

### Create-team command

1. Creating a new team

    1. Prerequisites: Team "Engineering" does not exist. Employee "E1001" exists.

    2. Test case: `create-team Engineering E1001`<br>
       Expected: New team "Engineering" is created. Success message confirms team creation.

2. Creating a team with an existing name

    1. Prerequisites: Team "Engineering" already exists. Employee "E1001" exists.

    2. Test case: `create-team Engineering E1001`<br>
       Expected: No team is created. Error message indicates duplicate team name.

3. Creating a team with invalid format

    1. Test case: `create-team 2312318`<br>
       Expected: No team is created. Error message shows the correct command format.

    2. Test case: `create-team`<br>
       Expected: No team is created. Error message indicates team name cannot be empty.

### Delete command

1. Deleting a person by employee ID

    1. Prerequisites: Person with ID E1001 exists in the list.

    2. Test case: `delete E1001`<br>
       Expected: Person E1001 is deleted from the list. Success message shows details of the deleted person.

2. Deleting a non-existent person

    1. Prerequisites: No person with ID E9999 exists.

    2. Test case: `delete E9999`<br>
       Expected: No person is deleted. Error message indicates the employee ID does not exist.

3. Invalid delete command format

    1. Test case: `delete`<br>
       Expected: No person is deleted. Error message shows the correct command format.

    2. Test case: `delete invalid`<br>
       Expected: No person is deleted. Error message indicates invalid employee ID format.

### Delete-team command

1. Deleting a team

    1. Prerequisites: Team "T0001" exists.

    2. Test case: `delete-team T0001`<br>
       Expected: Team "T0001" is deleted. Success message confirms deletion.

2. Deleting a team with members

    1. Prerequisites: Team "T0001" exists with members E1001 and E1002.

    2. Test case: `delete-team T0001`<br>
       Expected: Team "T0001" is deleted. Success message confirms deletion.

3. Deleting a non-existent team

    1. Prerequisites: Team "T9999" does not exist.

    2. Test case: `delete-team T9999`<br>
       Expected: No team is deleted. Error message indicates the team does not exist.

### Edit command

1. Editing a person's details

    1. Prerequisites: Person with ID E1001 exists.

    2. Test case: `edit E1001 -hp 91234567 -em newemail@example.com`<br>
       Expected: Person E1001's phone and email are updated. Success message shows the updated person's details.

2. Editing with no fields specified

    1. Test case: `edit E1001`<br>
       Expected: No changes made. Error message indicates at least one field to edit must be provided.

3. Editing a non-existent person

    1. Prerequisites: No person with ID E9999 exists.

    2. Test case: `edit E9999 -hp 91234567`<br>
       Expected: No person is edited. Error message indicates the employee ID does not exist.

4. Editing with invalid field values

    1. Test case: `edit E1001 -hp invalid`<br>
       Expected: No changes made. Error message indicates invalid phone number format.

### Exit command

1. Exiting the application

    1. Test case: `exit`<br>
       Expected: Application saves all data and closes.

### Help command

1. Opening the help window

    1. Test case: `help`<br>
       Expected: Help window opens with link to user guide.

### Import command

1. Importing from a valid file

    1. Prerequisites: A valid JSON file `contacts.json` exists in the specified path with proper format.

    2. Test case: `import contacts.json`<br>
       Expected: Valid contacts from the file are imported. Success message shows summary of imported contacts.

2. Importing from a non-existent file

    1. Test case: `import nonexistent.json`<br>
       Expected: No contacts are imported. Error message indicates the file does not exist.

3. Importing from an invalid file format

    1. Prerequisites: File `invalid.txt` exists but is not in JSON format.

    2. Test case: `import invalid.txt`<br>
       Expected: No contacts are imported. Error message indicates invalid file format.

4. Importing with duplicate entries

    1. Prerequisites: File contains contacts with GitHub usernames that already exist in the address book.

    2. Test case: `import contacts.json`<br>
       Expected: Only non-duplicate contacts are imported. Summary message shows skipped duplicates.

### List command

1. Listing all persons

    1. Prerequisites: Multiple persons exist in the address book.

    2. Test case: `list`<br>
       Expected: All persons are displayed in the list with their basic information.

2. Listing when address book is empty

    1. Prerequisites: Address book is empty.

    2. Test case: `list`<br>
       Expected: Message indicates no persons found or displays an empty list.

### Remove-from-team command

1. Removing an employee from a team

    1. Prerequisites: Person E1001 is a member of team "T0001".

    2. Test case: `remove-from-team T0001 E1001`<br>
       Expected: Person E1001 is removed from the "T0001" team. Success message confirms removal.

2. Removing an employee not in the team

    1. Prerequisites: Person E1001 exists but is not a member of team "T0001".

    2. Test case: `remove-from-team T0001 E1001`<br>
       Expected: No changes made. Error message indicates the person is not in the team.

3. Removing from a non-existent team

    1. Test case: `remove-from-team T0001 E1001`<br>
       Expected: No changes made. Error message indicates the team does not exist.

### Set-salary command

1. Setting salary for an employee

    1. Prerequisites: Person with ID E1001 exists.

    2. Test case: `set-salary E1001 5000`<br>
       Expected: Person E1001's salary is updated to 5000/month. Success message shows the updated salary.

2. Setting salary with invalid format

    1. Test case: `set-salary E1001 -1000`<br>
       Expected: No changes made. Error message indicates salary cannot be negative.

    2. Test case: `set-salary E1001 invalid`<br>
       Expected: No changes made. Error message indicates invalid salary format.

3. Setting salary for non-existent employee

    1. Test case: `set-salary E9999 5000`<br>
       Expected: No changes made. Error message indicates employee ID does not exist.

### Set-subteam command

1. Setting a subteam relationship

    1. Prerequisites: Teams "T0001" and "T0002" exist. No existing subteam relationship between them.

    2. Test case: `set-subteam T0001 T0002`<br>
       Expected: T0002 is set as a subteam of T0001. Success message confirms the relationship.

2. Setting subteam with non-existent teams

    1. Test case: `set-subteam T9999 T0002`<br>
       Expected: No changes made. Error message indicates parent team does not exist.

3. Creating circular dependency

    1. Prerequisites: T0002 is already parent of T0001.

    2. Test case: `set-subteam T0001 T0002`<br>
       Expected: No changes made. Error message indicates circular dependency.

4. Setting existing subteam relationship

    1. Prerequisites: T0002 is already a subteam of T0001.

    2. Test case: `set-subteam T0001 T0002`<br>
       Expected: No changes made. Error message indicates the relationship already exists.

### Sort command

1. Sorting persons by name

    1. Prerequisites: Multiple persons exist in the address book.

    2. Test case: `sort -name`<br>
       Expected: Persons list is sorted alphabetically by name. Sorted list is displayed.

2. Sorting persons by salary

    1. Test case: `sort -salary`<br>
       Expected: Persons list is sorted by salary (ascending or descending based on implementation). Sorted list is displayed.

3. Sorting with invalid field

    1. Test case: `sort -invalid`<br>
       Expected: No sorting performed. Error message lists valid sort fields.

4. Sorting empty address book

    1. Prerequisites: Address book is empty.

    2. Test case: `sort -name`<br>
       Expected: Message indicates no persons to sort.

### Tag command

1. Adding a tag to a person

    1. Prerequisites: Person with ID E1001 exists and does not have tag "developer".

    2. Test case: `tag E1001 developer`<br>
       Expected: Tag "developer" is added to person E1001. Success message confirms the tag addition.

2. Adding an existing tag

    1. Prerequisites: Person E1001 already has tag "developer".

    2. Test case: `tag E1001 developer`<br>
       Expected: No changes made. Error message indicates the tag already exists.

3. Adding multiple tags

    1. Test case: `tag E1001 developer senior`<br>
       Expected: Both tags are added to person E1001 (if not already present). Success message lists all added tags.

### Untag command

1. Removing tags from a person (case-insensitive)

    1. Prerequisites: Person E1001 has tags "Developer" and "Senior".

    2. Test case: `untag E1001 developer`<br>
       Expected: Tag "Developer" is removed from person E1001 (case-insensitive match). Success message lists removed tags and shows employee ID.

    3. Test case: `untag E1001 SENIOR developer`<br>
       Expected: Both tags are removed. Success message lists all removed tags.

2. Removing some non-existent tags

    1. Prerequisites: Person E1001 has tag "Developer" but not "Manager".

    2. Test case: `untag E1001 developer manager`<br>
       Expected: Tag "Developer" is removed. Warning message indicates "manager" tag was not found on the person.

3. Removing only non-existent tags

    1. Prerequisites: Person E1001 does not have tags "Manager" or "Lead".

    2. Test case: `untag E1001 manager lead`<br>
       Expected: No changes made. Error message indicates none of the specified tags exist on the person.

4. Removing tags from non-existent person

    1. Test case: `untag E9999 developer`<br>
       Expected: No changes made. Error message indicates employee ID does not exist.

### View command

1. Viewing person details

    1. Prerequisites: Person with full name "Alex Magnus" exists with complete details.

    2. Test case: `view Alex`<br>
       Expected: Detailed information panel displays showing all details of person "Alex Magnus" including name, phone, email, GitHub username, salary, tags, and teams.

2. Viewing non-existent person

   1. Prerequisites: No person named "Nullable" exists.
    
   2. Test case: `view Nullable`<br>
          Expected: No details displayed.

3. Invalid view command format

    1. Test case: `view`<br>
       Expected: Error message shows the correct command format.

### Saving data

1. Dealing with missing/corrupted data files

    1. Test case: Delete the data file at `[JAR file location]/data/addressbook.json` and restart the app.<br>
       Expected: App starts with sample data populated.

    2. Test case: Manually corrupt the data file by adding invalid JSON syntax and restart the app.<br>
       Expected: App starts with an empty address book. Error message may be logged.

2. Testing automatic saving

    1. Test case: Add a new person using the `add` command and immediately close the app using the window close button (not the `exit` command).<br>
       Expected: On restart, the newly added person should still be present in the address book.
