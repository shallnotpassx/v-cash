package com.gongbotao.vcash.shared.web;

import java.util.List;

public record ModuleOverview(
        String key,
        String name,
        String purpose,
        String status,
        List<String> dependsOn
) {
}
