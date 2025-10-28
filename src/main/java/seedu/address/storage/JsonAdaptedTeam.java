package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.team.Team;
import seedu.address.model.team.TeamName;

/**
 * Jackson-friendly version of {@link Team}.
 */
class JsonAdaptedTeam {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Team's %s field is missing!";

    private final String id;
    private final String name;
    private final String leaderId;
    private final List<String> members = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedTeam} with the given team details.
     */
    @JsonCreator
    public JsonAdaptedTeam(@JsonProperty("id") String id, @JsonProperty("name") String name,
                           @JsonProperty("leaderId") String leaderId,
                           @JsonProperty("members") List<String> members) {
        this.id = id;
        this.name = name;
        this.leaderId = leaderId;
        if (members != null) {
            this.members.addAll(members);
        }
    }

    /**
     * Converts a given {@code Team} into this class for Jackson use.
     */
    public JsonAdaptedTeam(Team source) {
        id = source.getId();
        name = source.getTeamName().teamName();
        leaderId = source.getLeaderId();
        if (source.getMembers() != null) {
            members.addAll(source.getMembers());
        }
    }

    /**
     * Converts this Jackson-friendly adapted team object into the model's {@code Team} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted team.
     */
    public Team toModelType() throws IllegalValueException {
        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "id"));
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    TeamName.class.getSimpleName()));
        }
        // This assumes TeamName has validation logic similar to other model classes.
        // if (!TeamName.isValidTeamName(name)) {
        //     throw new IllegalValueException(TeamName.MESSAGE_CONSTRAINTS);
        // }
        final TeamName modelTeamName = new TeamName(name);

        Team team = new Team(id, modelTeamName);

        if (leaderId != null) {
            team.withLeader(leaderId);
        }

        if (members != null) {
            team.withMembers(new ArrayList<>(members));
        }

        return team;
    }
}
