package com.gongbotao.vcash.shared.web;

import java.util.List;

public record ProjectOverview(
        String project,
        String stage,
        String architecture,
        String queryMode,
        List<String> primarySources,
        List<ModuleOverview> modules
) {
}
