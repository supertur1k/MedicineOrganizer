package dto;


public class FirstAidKitIdUsernameDTO2Users {
    public FirstAidKitIdUsernameDTO2Users(String username, String inviter, Long first_aid_kit_id) {
        this.username = username;
        this.inviter = inviter;
        this.first_aid_kit_id = first_aid_kit_id;
    }

    String username;
    String inviter;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public Long getFirst_aid_kit_id() {
        return first_aid_kit_id;
    }

    public void setFirst_aid_kit_id(Long first_aid_kit_id) {
        this.first_aid_kit_id = first_aid_kit_id;
    }

    Long first_aid_kit_id;
}
