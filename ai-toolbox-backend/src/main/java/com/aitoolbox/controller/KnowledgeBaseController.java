package com.aitoolbox.controller;

import com.aitoolbox.common.Result;
import com.aitoolbox.entity.KnowledgeBase;
import com.aitoolbox.entity.User;
import com.aitoolbox.service.KnowledgeBaseService;
import com.aitoolbox.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeBaseController {

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public Result<?> pageKnowledge(
            @RequestParam long current,
            @RequestParam long size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        Page<KnowledgeBase> page = knowledgeBaseService.pageKnowledge(current, size, category, keyword);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getKnowledgeDetail(@PathVariable Long id) {
        KnowledgeBase knowledge = knowledgeBaseService.getKnowledgeDetail(id);
        return Result.success(knowledge);
    }

    @PostMapping("/{id}/like")
    public Result<?> incrementLikeCount(@PathVariable Long id) {
        knowledgeBaseService.incrementLikeCount(id);
        return Result.success();
    }

    @PostMapping
    public Result<?> addKnowledge(@RequestBody Map<String, Object> requestData) {
        KnowledgeBase knowledge = new KnowledgeBase();
        
        // 设置基本字段
        knowledge.setTitle((String) requestData.get("title"));
        knowledge.setContent((String) requestData.get("content"));
        knowledge.setCategory((String) requestData.get("category"));
        knowledge.setTags((String) requestData.get("tags"));
        
        // 设置默认值
        knowledge.setIsPublished(1);
        knowledge.setViewCount(0);
        knowledge.setLikeCount(0);
        
        knowledgeBaseService.save(knowledge);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> updateKnowledge(@PathVariable Long id, @RequestBody Map<String, Object> requestData) {
        KnowledgeBase knowledge = knowledgeBaseService.getById(id);
        if (knowledge == null) {
            return Result.error("知识库不存在");
        }
        
        // 更新字段
        if (requestData.containsKey("title")) {
            knowledge.setTitle((String) requestData.get("title"));
        }
        if (requestData.containsKey("content")) {
            knowledge.setContent((String) requestData.get("content"));
        }
        if (requestData.containsKey("category")) {
            knowledge.setCategory((String) requestData.get("category"));
        }
        if (requestData.containsKey("tags")) {
            knowledge.setTags((String) requestData.get("tags"));
        }
        
        knowledgeBaseService.updateById(knowledge);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteKnowledge(@PathVariable Long id) {
        knowledgeBaseService.removeById(id);
        return Result.success();
    }

    @GetMapping("/admin/all")
    public Result<?> getAllKnowledge() {
        List<KnowledgeBase> knowledgeList = knowledgeBaseService.getAllKnowledge();
        return Result.success(knowledgeList);
    }
}