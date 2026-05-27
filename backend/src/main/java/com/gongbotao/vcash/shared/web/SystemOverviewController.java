package com.gongbotao.vcash.shared.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemOverviewController {

    @GetMapping("/overview")
    public ApiResponse<ProjectOverview> overview() {
        ProjectOverview overview = new ProjectOverview(
                "v-cash",
                "skeleton",
                "single-spring-boot-ddd",
                "local-only-by-default",
                List.of("eastmoney", "cninfo", "hkexnews"),
                List.of(
                        new ModuleOverview(
                                "stock-registry",
                                "股票主数据",
                                "统一维护 A 股和港股股票身份与基础资料",
                                "planned",
                                List.of()
                        ),
                        new ModuleOverview(
                                "data-ingestion-sync",
                                "数据拉取与同步",
                                "按指定代码触发数据拉取与同步记录",
                                "planned",
                                List.of("stock-registry")
                        ),
                        new ModuleOverview(
                                "financial-screening",
                                "财务筛选",
                                "维护历史财务与筛选快照，支持多指标筛选",
                                "planned",
                                List.of("stock-registry", "data-ingestion-sync")
                        ),
                        new ModuleOverview(
                                "announcement-center",
                                "公告中心",
                                "维护公告元数据与原文链接，支撑公告检索与人工解析",
                                "planned",
                                List.of("stock-registry")
                        ),
                        new ModuleOverview(
                                "deployment",
                                "部署",
                                "提供本地优先的 Docker Compose 交付方式",
                                "planned",
                                List.of()
                        )
                )
        );

        return ApiResponse.ok(overview);
    }
}
