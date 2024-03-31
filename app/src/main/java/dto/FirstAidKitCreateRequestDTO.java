package dto;

import java.util.Collection;

public class FirstAidKitCreateRequestDTO {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName_of_the_first_aid_kit() {
        return name_of_the_first_aid_kit;
    }

    public void setName_of_the_first_aid_kit(String name_of_the_first_aid_kit) {
        this.name_of_the_first_aid_kit = name_of_the_first_aid_kit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String username;
    private String name_of_the_first_aid_kit;
    private String description;

    public FirstAidKitCreateRequestDTO(String username, String name_of_the_first_aid_kit, String description) {
        this.username = username;
        this.name_of_the_first_aid_kit = name_of_the_first_aid_kit;
        this.description = description;
    }
}
