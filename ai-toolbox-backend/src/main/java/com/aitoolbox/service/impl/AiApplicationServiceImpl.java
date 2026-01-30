package com.aitoolbox.service.impl;

import com.aitoolbox.entity.AiApplication;
import com.aitoolbox.mapper.AiApplicationMapper;
import com.aitoolbox.service.AiApplicationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AiApplicationServiceImpl extends ServiceImpl<AiApplicationMapper, AiApplication> implements AiApplicationService {

    @Autowired
    private AiApplicationMapper aiApplicationMapper;

    @Override
    public Page<AiApplication> pageApplications(long current, long size, String category, String keyword) {
        Page<AiApplication> page = new Page<>(current, size);
        LambdaQueryWrapper<AiApplication> queryWrapper = new LambdaQueryWrapper<>();

        if (category != null && !category.isEmpty()) {
            queryWrapper.eq(AiApplication::getCategory, category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> {
                wrapper.like(AiApplication::getName, keyword)
                       .or().like(AiApplication::getDescription, keyword);
            });
        }

        queryWrapper.eq(AiApplication::getStatus, 1);
        queryWrapper.orderByDesc(AiApplication::getViewCount);

        return page(page, queryWrapper);
    }

    @Override
    public void incrementViewCount(Long id) {
        AiApplication application = getById(id);
        if (application != null) {
            application.setViewCount(application.getViewCount() + 1);
            updateById(application);
        }
    }

    @Override
    public AiApplication getApplicationDetail(Long id) {
        incrementViewCount(id);
        return getById(id);
    }

    @Override
    public List<AiApplication> getAllApplications() {
        return list();
    }
}