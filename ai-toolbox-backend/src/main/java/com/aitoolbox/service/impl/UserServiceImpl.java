package com.aitoolbox.service.impl;

import com.aitoolbox.common.BusinessException;
import com.aitoolbox.entity.User;
import com.aitoolbox.mapper.UserMapper;
import com.aitoolbox.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(403, "用户已被禁用");
        }
        return user;
    }

    @Override
    public User register(User user) {
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new BusinessException(400, "用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        user.setStatus(1);
        save(user);
        return user;
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public List<User> listUsers(int current, int size) {
        Page<User> page = new Page<>(current, size);
        return page(page).getRecords();
    }

    @Override
    public User getUserById(Long id) {
        return getById(id);
    }

    @Override
    public User updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        updateById(user);
        return getById(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        removeById(id);
    }
}