package com.acap.rc

import com.acap.rc.event.OnEventDialog
import com.acap.ec.listener.OnEventLogListener


fun main() {
    MyApiService.getResponseBody() //                .chain(MyApiService.getModelError())
        .chain(MyApiService.getModel())
        .listener(OnEventDialog(null))
        .listener(OnEventLogListener("请求日志"))
        .start()
}
