package com.gongbotao.vcash.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongbotao.vcash.domain.financial.entity.StockFinancial;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockFinancialMapper extends BaseMapper<StockFinancial> {
}