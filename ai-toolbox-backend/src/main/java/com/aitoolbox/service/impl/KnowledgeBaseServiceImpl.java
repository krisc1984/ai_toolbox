package com.aitoolbox.service.impl;

import com.aitoolbox.entity.KnowledgeBase;
import com.aitoolbox.mapper.KnowledgeBaseMapper;
import com.aitoolbox.service.KnowledgeBaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase> implements KnowledgeBaseService {

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Override
    public Page<KnowledgeBase> pageKnowledge(long current, long size, String category, String keyword) {
        Page<KnowledgeBase> page = new Page<>(current, size);
        LambdaQueryWrapper<KnowledgeBase> queryWrapper = new LambdaQueryWrapper<>();

        if (category != null && !category.isEmpty()) {
            queryWrapper.eq(KnowledgeBase::getCategory, category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> {
                wrapper.like(KnowledgeBase::getTitle, keyword)
                       .or().like(KnowledgeBase::getContent, keyword)
                       .or().like(KnowledgeBase::getTags, keyword);
            });
        }

        queryWrapper.eq(KnowledgeBase::getIsPublished, 1);
        queryWrapper.orderByDesc(KnowledgeBase::getViewCount);

        return page(page, queryWrapper);
    }

    @Override
    public void incrementViewCount(Long id) {
        KnowledgeBase knowledgeBase = getById(id);
        if (knowledgeBase != null) {
            knowledgeBase.setViewCount(knowledgeBase.getViewCount() + 1);
            updateById(knowledgeBase);
        }
    }

    @Override
    public void incrementLikeCount(Long id) {
        KnowledgeBase knowledgeBase = getById(id);
        if (knowledgeBase != null) {
            knowledgeBase.setLikeCount(knowledgeBase.getLikeCount() + 1);
            updateById(knowledgeBase);
        }
    }

    @Override
    public KnowledgeBase getKnowledgeDetail(Long id) {
        incrementViewCount(id);
        return getById(id);
    }

    @Override
    public List<KnowledgeBase> getAllKnowledge() {
        return list();
    }
}