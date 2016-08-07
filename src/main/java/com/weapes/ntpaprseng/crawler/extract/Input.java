package com.weapes.ntpaprseng.crawler.extract;

/**
 * Created by lawrence on 16/8/7.
 */
public class Input {
    public static enum Type {
        FILE,
        WebPage
    }

    private Type type;

    private Object value;

    public Input(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
