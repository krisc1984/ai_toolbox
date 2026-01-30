package com.aitoolbox.service;

import com.aitoolbox.entity.AiNews;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface AiNewsService extends IService<AiNews> {
    Page<AiNews> pageNews(long current, long size, String category, String keyword);
    void incrementViewCount(Long id);
    AiNews getNewsDetail(Long id);
    List<AiNews> getHotNews();
}