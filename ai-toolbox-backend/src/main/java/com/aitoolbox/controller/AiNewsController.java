package com.aitoolbox.controller;

import com.aitoolbox.common.Result;
import com.aitoolbox.entity.AiNews;
import com.aitoolbox.service.AiNewsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public class AiNewsController {

    @Autowired
    private AiNewsService aiNewsService;

    @GetMapping("/page")
    public Result<?> pageNews(
            @RequestParam long current,
            @RequestParam long size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        Page<AiNews> page = aiNewsService.pageNews(current, size, category, keyword);
        return Result.success(page);
    }

    @GetMapping("/hot")
    public Result<?> getHotNews() {
        return Result.success(aiNewsService.getHotNews());
    }

    @GetMapping("/{id}")
    public Result<?> getNewsDetail(@PathVariable Long id) {
        AiNews news = aiNewsService.getNewsDetail(id);
        return Result.success(news);
    }

    @PostMapping("/{id}/view")
    public Result<?> incrementViewCount(@PathVariable Long id) {
        aiNewsService.incrementViewCount(id);
        return Result.success();
    }

    @PostMapping
    public Result<?> addNews(@RequestBody AiNews news) {
        aiNewsService.save(news);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> updateNews(@PathVariable Long id, @RequestBody AiNews news) {
        news.setId(id);
        aiNewsService.updateById(news);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteNews(@PathVariable Long id) {
        aiNewsService.removeById(id);
        return Result.success();
    }

    @GetMapping("/admin/all")
    public Result<?> getAllNews() {
        return Result.success(aiNewsService.list());
    }
}