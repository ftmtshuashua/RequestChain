RequestChain
=====
[![](https://jitpack.io/v/ftmtshuashua/RequestChain.svg)](https://jitpack.io/#ftmtshuashua/RequestChain)
[![](https://img.shields.io/badge/android-5.0%2B-blue)]()
[![](https://img.shields.io/badge/jdk-1.8%2B-blue)]()
[![License Apache2.0](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

RequestChain是一个基于 EventChain + Retrofit + OkHttp 实现的网络请求框架,只需要简单的配置即可快速搭建请求环境

Download
-------
```
repositories {
    maven { url 'https://www.jitpack.io' }
}

dependencies {
    implementation 'com.github.ftmtshuashua:EventChain:2.0.0-alpha3'
    implementation 'com.github.ftmtshuashua:RequestChain:2.0.0-alpha3'
    annotationProcessor 'com.github.ftmtshuashua.RequestChain:compiler:2.0.0-alpha3'

    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.9.1'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'  //自动引入Gson
}
```



Use
--------


相关资料
--------
> [OkHttp](https://github.com/square/okhttp)

> [Retrofit](https://github.com/square/retrofit)

> [EventCHain](https://github.com/ftmtshuashua/EventChain)