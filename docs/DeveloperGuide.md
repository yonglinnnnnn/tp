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

*{More to be added}*

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

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete E1001`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete E1002`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
