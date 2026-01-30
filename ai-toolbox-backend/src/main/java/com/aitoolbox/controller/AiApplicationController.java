package com.aitoolbox.controller;

import com.aitoolbox.common.Result;
import com.aitoolbox.entity.AiApplication;
import com.aitoolbox.service.AiApplicationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class AiApplicationController {

    @Autowired
    private AiApplicationService aiApplicationService;

    @GetMapping("/page")
    public Result<?> pageApplications(
            @RequestParam long current,
            @RequestParam long size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        Page<AiApplication> page = aiApplicationService.pageApplications(current, size, category, keyword);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getApplicationDetail(@PathVariable Long id) {
        AiApplication application = aiApplicationService.getApplicationDetail(id);
        return Result.success(application);
    }

    @PostMapping("/{id}/view")
    public Result<?> incrementViewCount(@PathVariable Long id) {
        aiApplicationService.incrementViewCount(id);
        return Result.success();
    }

    @PostMapping
    public Result<?> addApplication(@RequestBody AiApplication application) {
        aiApplicationService.save(application);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> updateApplication(@PathVariable Long id, @RequestBody AiApplication application) {
        application.setId(id);
        aiApplicationService.updateById(application);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteApplication(@PathVariable Long id) {
        aiApplicationService.removeById(id);
        return Result.success();
    }

    @GetMapping("/admin/all")
    public Result<?> getAllApplications() {
        List<AiApplication> applications = aiApplicationService.getAllApplications();
        return Result.success(applications);
    }
}