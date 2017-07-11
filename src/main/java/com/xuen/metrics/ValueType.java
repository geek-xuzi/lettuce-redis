package com.xuen.metrics;

public enum ValueType {

    count("数值"),
    //
    min("最小值"),
    //
    max("最大值"),
    //
    mean("平均值"),
    //
    std("方差"),
    //
    p75(""),
    //
    p98(""),
    //
    mean_rate("TPS"),
    //
    m1_rate("1分钟TPS"),
    //
    m5_rate("5分钟TPS"),
    //
    m15_rate("15分钟TPS");

    private final String text;

    private ValueType(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
