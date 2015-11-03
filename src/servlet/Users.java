package servlet;

import java.util.ArrayList;
import java.util.List;


public class Users {

    private List<String> users = new ArrayList<>();

    public synchronized List<String> getUsers() {
        return users;
    }

    public synchronized void setUsers(List<String> users) {
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
