package com.gongbotao.vcash.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongbotao.vcash.domain.stock.entity.StockBasic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockBasicMapper extends BaseMapper<StockBasic> {
}