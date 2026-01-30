package com.aitoolbox.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_news")
public class AiNews {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("summary")
    private String summary;

    @TableField("content")
    private String content;

    @TableField("source")
    private String source;

    @TableField("category")
    private String category;

    @TableField("cover_image")
    private String coverImage;

    @TableField("tags")
    private String tags;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("is_hot")
    private Integer isHot;

    @TableField("is_top")
    private Integer isTop;

    @TableField("publish_time")
    private LocalDateTime publishTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}