package com.aitoolbox.service;

import com.aitoolbox.entity.AiApplication;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface AiApplicationService extends IService<AiApplication> {
    Page<AiApplication> pageApplications(long current, long size, String category, String keyword);
    void incrementViewCount(Long id);
    AiApplication getApplicationDetail(Long id);
    List<AiApplication> getAllApplications();
}