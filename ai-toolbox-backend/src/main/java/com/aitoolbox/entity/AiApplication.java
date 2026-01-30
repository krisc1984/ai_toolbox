package com.aitoolbox.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ai_application")
public class AiApplication {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("category")
    private String category;

    @TableField("icon")
    private String icon;

    @TableField("url")
    private String url;

    @TableField("is_free")
    private Integer isFree;

    @TableField("price")
    private BigDecimal price;

    @TableField("rating")
    private BigDecimal rating;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("status")
    private Integer status;

    @TableField("created_by")
    private Long createdBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}