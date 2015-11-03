package servlet;

import java.util.ArrayList;


public class Users {

    private ArrayList<String> users = new ArrayList<>();

    public synchronized ArrayList<String> getUsers() {
        return users;
    }

    public synchronized void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Users:<br>");
        for (String name : getUsers()) {
            sb.append(name).append("<br>");
        }
        return sb.toString();
    }
}
