package com.gongbotao.vcash.domain.financial.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("stock_financial")
public class StockFinancial {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long stockId;

    private LocalDate reportPeriod;

    private BigDecimal revenue;

    private BigDecimal netProfit;

    private BigDecimal roe;

    private BigDecimal pe;

    private BigDecimal pb;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}