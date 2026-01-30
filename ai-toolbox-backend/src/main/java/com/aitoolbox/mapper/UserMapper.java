package com.aitoolbox.mapper;

import com.aitoolbox.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(String username);
}