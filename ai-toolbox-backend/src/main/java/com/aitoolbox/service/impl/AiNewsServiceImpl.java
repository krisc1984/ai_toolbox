package com.aitoolbox.service.impl;

import com.aitoolbox.entity.AiNews;
import com.aitoolbox.mapper.AiNewsMapper;
import com.aitoolbox.service.AiNewsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AiNewsServiceImpl extends ServiceImpl<AiNewsMapper, AiNews> implements AiNewsService {

    @Autowired
    private AiNewsMapper aiNewsMapper;

    @Override
    public Page<AiNews> pageNews(long current, long size, String category, String keyword) {
        Page<AiNews> page = new Page<>(current, size);
        LambdaQueryWrapper<AiNews> queryWrapper = new LambdaQueryWrapper<>();

        if (category != null && !category.isEmpty()) {
            queryWrapper.eq(AiNews::getCategory, category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> {
                wrapper.like(AiNews::getTitle, keyword)
                       .or().like(AiNews::getSummary, keyword)
                       .or().like(AiNews::getContent, keyword);
            });
        }

        queryWrapper.orderByDesc(AiNews::getIsTop)
                   .orderByDesc(AiNews::getIsHot)
                   .orderByDesc(AiNews::getPublishTime);

        return page(page, queryWrapper);
    }

    @Override
    public void incrementViewCount(Long id) {
        AiNews aiNews = getById(id);
        if (aiNews != null) {
            aiNews.setViewCount(aiNews.getViewCount() + 1);
            updateById(aiNews);
        }
    }

    @Override
    public AiNews getNewsDetail(Long id) {
        incrementViewCount(id);
        return getById(id);
    }

    @Override
    public List<AiNews> getHotNews() {
        LambdaQueryWrapper<AiNews> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiNews::getIsHot, 1)
                   .orderByDesc(AiNews::getViewCount)
                   .last("limit 5");
        return list(queryWrapper);
    }
}