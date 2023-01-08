package model;

import exception.FileExc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repository {
    private UserMapper mapper = new UserMapper();
    private FileOperation fileOperation;

    public Repository(FileOperation fileOperation) {
        this.fileOperation = fileOperation;
    }

    public void createUser(User user) {
        List<User> users = this.getUsersWithLastName(user.getLastName());
        this.saveUser(user, users);
    }

    public List<User> getUsersWithLastName(String lastName) {
        this.fileOperation.setFileName(lastName);
        List<String> lines = this.fileOperation.readAllLines();
        List<User> users = new ArrayList<>();
        Iterator iterator = lines.iterator();

        while(iterator.hasNext()) {
            String line = (String)iterator.next();
            try {
                users.add(this.mapper.map(line));
            } catch (FileExc e) {
                e.printStackTrace();
            }
        }
        return users;
    }
    public void saveUser(User user, List<User> users) {
        users.add(user);
        this.saveUsers(users);
    }

    private void saveUsers(List<User> users) {
        if (users.isEmpty()) {
            return;
        }
        List<String> lines = new ArrayList<>();
        Iterator iterator = users.iterator();

        while(iterator.hasNext()) {
            User item = (User)iterator.next();
            lines.add(this.mapper.map(item));
        }
        this.fileOperation.setFileName(users.get(0).getLastName());
        this.fileOperation.saveAllLines(lines);
    }
}