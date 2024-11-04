package edu.uiuc.cs427app;

public class UserHelper {

    String username, password, ui, cities;

    public UserHelper() {
    }

    public UserHelper(String username, String password, String ui, String cities) {
        this.username = username;
        this.password = password;
        this.ui = ui;
        this.cities = cities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getUI() {
        return ui;
    }

    public void setUI(String ui) {
        this.ui = ui;
    }
    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }
}
