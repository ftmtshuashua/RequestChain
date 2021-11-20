package com.acap.demo.api;

import com.acap.rc.annotation.service.VariableUrl;

/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/11/20 23:30
 * </pre>
 */
public class RandomUrlConfig implements VariableUrl {
    @Override
    public String url() {
        return String.format("http://%s", (int) (Math.random() * 1000));
    }
}
