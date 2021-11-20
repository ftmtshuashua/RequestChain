package com.acap.demo

import com.acap.demo.event.OnEventDialog
import com.acap.demo.model.BaseModel
import com.acap.ec.Events
import com.acap.ec.action.Apply
import com.acap.ec.listener.OnEventLogListener


fun main() {
    MyApiService.getResponseBody() //                .chain(MyApiService.getModelError())
        .chain(MyApiService.getModel())
        .listener(OnEventDialog(null))
        .listener(OnEventLogListener("请求日志"))
        .start()
}
