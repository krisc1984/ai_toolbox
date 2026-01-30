package com.aitoolbox.service;

import com.aitoolbox.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface UserService extends IService<User> {
    User login(String username, String password);
    User register(User user);
    User getByUsername(String username);
    List<User> listUsers(int current, int size);
    User getUserById(Long id);
    User updateUser(User user);
    void deleteUser(Long id);
}