package com.cms.Services;

import com.cms.Models.User;

import java.util.List;
import java.util.Optional;

public interface userService {

    public User saveUser(User user);
    public Optional<User> getUserById(String id);
    public Optional<User> updateUser(User user);
    public void deleteUser(String id);
    public boolean isUserPresent(String userId);
    public boolean isUserPresentByEmail(String email);
    public List<User> getAllUsers();
    public User getUserByEmail(String email);

}
