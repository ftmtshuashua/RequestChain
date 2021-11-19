package com.acap.demo

import com.acap.ec.Events
import com.acap.ec.action.Apply


fun main() {

    Events.create(Apply<Any, String> { "" })
        .chain(MyApiService.getResponseBody())
        .chain(MyApiService.getModel())
        .apply(Apply { it.isSuccessful })

}
