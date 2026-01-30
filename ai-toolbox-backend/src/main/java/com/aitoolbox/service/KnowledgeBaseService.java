package com.aitoolbox.service;

import com.aitoolbox.entity.KnowledgeBase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface KnowledgeBaseService extends IService<KnowledgeBase> {
    Page<KnowledgeBase> pageKnowledge(long current, long size, String category, String keyword);
    void incrementViewCount(Long id);
    void incrementLikeCount(Long id);
    KnowledgeBase getKnowledgeDetail(Long id);
    List<KnowledgeBase> getAllKnowledge();
}