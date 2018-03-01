package com.am.socket.model;

public class User {
    private String userName;
    private String password;

    /**
     *@return  password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return username
     */
    public String getUsername() {
        return userName;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.userName = username;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
