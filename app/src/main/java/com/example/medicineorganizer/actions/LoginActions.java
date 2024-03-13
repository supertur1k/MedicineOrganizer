package com.example.medicineorganizer.actions;

import org.apache.commons.lang3.StringUtils;

public class LoginActions {

    public boolean isCredentialsAreValid(String username, String password) {

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) return false;

        if (username.equals("1") && password.equals("1")) return true;
        return false;
    }
}
