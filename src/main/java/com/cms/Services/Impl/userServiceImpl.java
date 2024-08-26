package com.cms.Services.Impl;

import com.cms.Helpers.ResourceNotFoundException;
import com.cms.Models.User;
import com.cms.Repositories.userRepository;
import com.cms.Services.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private userRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        if (user != null) {
            String userId = UUID.randomUUID().toString();
            user.setUserId(userId);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            //set user role
            user.setRoleList(List.of("ROLE_USER"));

            logger.info(user.getProvider().toString());

            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {

        User user1 = this.getUserById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        //update user2 from user
        user1.setName(user.getName());
        user1.setAbout(user.getAbout());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setPhone(user.getPhone());
        user1.setProfilePic(user.getProfilePic());
        user1.setEnabled(user.isEnabled());
        user1.setEmailVerified(user.isEmailVerified());
        user1.setPhoneVerified(user.isPhoneVerified());
        user1.setGender(user.getGender());
        user1.setProviderUserId(user.getProviderUserId());
        user1.setProvider(user.getProvider());

        return Optional.ofNullable(saveUser(user1));
    }

    @Override
    public void deleteUser(String id) {
        User user1 = this.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        userRepository.delete(user1);

    }

    @Override
    public boolean isUserPresent(String userId) {
        User user1 = this.getUserById(userId).orElse(null);
        return user1 != null;
    }

    @Override
    public boolean isUserPresentByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
