package practice.spring_gym_api.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateMultipleMembersRequest {

    @NotNull(message = "List of IDs must not be null")
    @Size(min = 2, message = "At least two IDs must be provided")
    private List<Long> ids;

    @NotNull(message = "List of names must not be null")
    @Size(min = 2, message = "At least two names must be provided")
    private List<@NotBlank(message = "Name must not be blank") String> names;

    @NotNull(message = "List of emails must not be null")
    @Size(min = 2, message = "At least two emails must be provided")
    private List<@NotBlank(message = "Email must not be blank") String> emails;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
