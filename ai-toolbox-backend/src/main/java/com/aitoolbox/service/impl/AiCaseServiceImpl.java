package com.aitoolbox.service.impl;

import com.aitoolbox.entity.AiCase;
import com.aitoolbox.mapper.AiCaseMapper;
import com.aitoolbox.service.AiCaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiCaseServiceImpl extends ServiceImpl<AiCaseMapper, AiCase> implements AiCaseService {

    @Autowired
    private AiCaseMapper aiCaseMapper;

    @Override
    public Page<AiCase> pageCases(long current, long size, String industry, String keyword) {
        Page<AiCase> page = new Page<>(current, size);
        LambdaQueryWrapper<AiCase> queryWrapper = new LambdaQueryWrapper<>();

        if (industry != null && !industry.isEmpty()) {
            queryWrapper.eq(AiCase::getIndustry, industry);
        }

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> {
                wrapper.like(AiCase::getTitle, keyword)
                       .or().like(AiCase::getDescription, keyword)
                       .or().like(AiCase::getCompanyName, keyword);
            });
        }

        queryWrapper.eq(AiCase::getStatus, 1);
        queryWrapper.orderByDesc(AiCase::getViewCount);

        return page(page, queryWrapper);
    }

    @Override
    public void incrementViewCount(Long id) {
        AiCase aiCase = getById(id);
        if (aiCase != null) {
            aiCase.setViewCount(aiCase.getViewCount() + 1);
            updateById(aiCase);
        }
    }

    @Override
    public void incrementLikeCount(Long id) {
        AiCase aiCase = getById(id);
        if (aiCase != null) {
            aiCase.setLikeCount(aiCase.getLikeCount() + 1);
            updateById(aiCase);
        }
    }

    @Override
    public AiCase getCaseDetail(Long id) {
        incrementViewCount(id);
        return getById(id);
    }
}