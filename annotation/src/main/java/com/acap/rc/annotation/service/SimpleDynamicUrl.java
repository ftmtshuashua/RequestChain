package com.acap.rc.annotation.service;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/11/18 16:39
 * </pre>
 */
public class SimpleDynamicUrl implements DynamicUrl{
    private String url;

    public SimpleDynamicUrl(String url) {
        this.url = url;
    }

    @Override
    public String url() {
        return url;
    }
}
