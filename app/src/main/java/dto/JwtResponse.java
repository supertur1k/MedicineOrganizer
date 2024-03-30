package dto;

import com.google.gson.annotations.SerializedName;

public class JwtResponse {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JwtResponse(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    @SerializedName("id")
    private Long id;
}
