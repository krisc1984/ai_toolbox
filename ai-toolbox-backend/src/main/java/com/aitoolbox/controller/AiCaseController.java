package com.aitoolbox.controller;

import com.aitoolbox.common.Result;
import com.aitoolbox.entity.AiCase;
import com.aitoolbox.service.AiCaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cases")
public class AiCaseController {

    @Autowired
    private AiCaseService aiCaseService;

    @GetMapping("/page")
    public Result<?> pageCases(
            @RequestParam long current,
            @RequestParam long size,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String keyword) {
        Page<AiCase> page = aiCaseService.pageCases(current, size, industry, keyword);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getCaseDetail(@PathVariable Long id) {
        AiCase aiCase = aiCaseService.getCaseDetail(id);
        return Result.success(aiCase);
    }

    @PostMapping("/{id}/view")
    public Result<?> incrementViewCount(@PathVariable Long id) {
        aiCaseService.incrementViewCount(id);
        return Result.success();
    }

    @PostMapping
    public Result<?> addCase(@RequestBody AiCase aiCase) {
        aiCaseService.save(aiCase);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> updateCase(@PathVariable Long id, @RequestBody AiCase aiCase) {
        aiCase.setId(id);
        aiCaseService.updateById(aiCase);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteCase(@PathVariable Long id) {
        aiCaseService.removeById(id);
        return Result.success();
    }

    @GetMapping("/admin/all")
    public Result<?> getAllCases() {
        List<AiCase> cases = aiCaseService.list();
        return Result.success(cases);
    }
}