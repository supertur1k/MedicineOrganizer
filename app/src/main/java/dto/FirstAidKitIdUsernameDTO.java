package dto;

public class FirstAidKitIdUsernameDTO {
    private String username;
    private Long first_aid_kit_id;

    public String getUsername() {
        return username;
    }

    public FirstAidKitIdUsernameDTO(String username, Long first_aid_kit_id) {
        this.username = username;
        this.first_aid_kit_id = first_aid_kit_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getFirst_aid_kit_id() {
        return first_aid_kit_id;
    }

    public void setFirst_aid_kit_id(Long first_aid_kit_id) {
        this.first_aid_kit_id = first_aid_kit_id;
    }
}
