package com.acap.rc.model;

import com.acap.rc.adapter.ApiBody;

public class BaseModel<T> implements ApiBody {
    public int code;
    public T result;
    public String msg;

    @Override
    public boolean isSuccessful() {
        return code == 0;
    }

    @Override
    public Exception getError() {
        return new Exception(msg);
    }
}
