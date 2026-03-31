package com.gongbotao.vcash.domain.enums;

public enum Market {
    A("A股", ""),
    HK("港股", "5");

    private final String desc;
    private final String prefix;

    Market(String desc, String prefix) {
        this.desc = desc;
        this.prefix = prefix;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrefix() {
        return prefix;
    }

    public static Market of(String value) {
        if (value == null || value.isEmpty()) {
            return A;
        }
        for (Market market : values()) {
            if (market.name().equalsIgnoreCase(value) || market.desc.equals(value)) {
                return market;
            }
        }
        return A;
    }
}