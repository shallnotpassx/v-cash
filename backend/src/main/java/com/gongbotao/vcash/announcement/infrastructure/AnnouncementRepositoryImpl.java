package com.gongbotao.vcash.announcement.infrastructure;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gongbotao.vcash.announcement.domain.Announcement;
import com.gongbotao.vcash.announcement.domain.AnnouncementRepository;
import com.gongbotao.vcash.shared.domain.StockIdentity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class AnnouncementRepositoryImpl implements AnnouncementRepository {
    private final AnnouncementMapper mapper;

    public AnnouncementRepositoryImpl(AnnouncementMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Announcement> findByStock(StockIdentity id) {
        return mapper.selectList(new LambdaQueryWrapper<AnnouncementEntity>()
                .eq(AnnouncementEntity::getMarket, id.market())
                .eq(AnnouncementEntity::getStockCode, id.stockCode()))
                .stream().map(AnnouncementEntity::toDomain).toList();
    }

    @Override
    public List<Announcement> findAll() {
        return mapper.selectList(null).stream()
                .map(AnnouncementEntity::toDomain).toList();
    }

    @Override
    public Optional<Announcement> findById(String id) {
        return Optional.ofNullable(mapper.selectById(id))
                .map(AnnouncementEntity::toDomain);
    }

    @Override
    public List<Announcement> search(String keyword, String sourceSite,
                                      LocalDate dateFrom, LocalDate dateTo) {
        var qw = new LambdaQueryWrapper<AnnouncementEntity>();
        if (keyword != null && !keyword.isBlank()) {
            qw.like(AnnouncementEntity::getTitle, keyword);
        }
        if (sourceSite != null && !sourceSite.isBlank()) {
            qw.eq(AnnouncementEntity::getSourceSite, sourceSite);
        }
        if (dateFrom != null) {
            qw.ge(AnnouncementEntity::getPublishDate, dateFrom);
        }
        if (dateTo != null) {
            qw.le(AnnouncementEntity::getPublishDate, dateTo);
        }
        qw.orderByDesc(AnnouncementEntity::getPublishDate);
        return mapper.selectList(qw).stream()
                .map(AnnouncementEntity::toDomain).toList();
    }
}
