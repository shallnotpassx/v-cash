package com.gongbotao.vcash.domain.announcement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("stock_announcement")
public class StockAnnouncement {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long stockId;

    private String title;

    private String announcementType;

    private LocalDate publishDate;

    private String sourceUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}