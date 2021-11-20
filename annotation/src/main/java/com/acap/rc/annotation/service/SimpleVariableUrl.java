package com.acap.rc.annotation.service;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/11/18 16:39
 * </pre>
 */
public class SimpleVariableUrl implements VariableUrl {
    private final String url;

    public SimpleVariableUrl(String url) {
        this.url = url;
    }

    @Override
    public String url() {
        return url;
    }
}
