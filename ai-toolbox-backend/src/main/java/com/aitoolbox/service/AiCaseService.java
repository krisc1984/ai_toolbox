package com.aitoolbox.service;

import com.aitoolbox.entity.AiCase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface AiCaseService extends IService<AiCase> {
    Page<AiCase> pageCases(long current, long size, String industry, String keyword);
    void incrementViewCount(Long id);
    void incrementLikeCount(Long id);
    AiCase getCaseDetail(Long id);
}