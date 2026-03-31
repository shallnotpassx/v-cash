package com.gongbotao.vcash.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("stock_basic")
public class StockBasic {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String stockCode;

    private String stockName;

    private String market;

    private String industry;

    private LocalDate listDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}