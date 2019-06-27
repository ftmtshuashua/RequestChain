# RequestChain
基于EventChain的Android请求事件链，请求通过Retrofit+RxJava+OkHttp实现


>链式请求

>并发请求

>事件链监听



## 使用
使用方式参考：配合Java8的lambda语法使用逻辑更清晰
![MainActivity](https://github.com/ftmtshuashua/RequestChain/blob/master/app/src/main/java/support/lfp/requestchain/MainActivity.java)


## 配置依赖

在项目的build.gradle中添加
```
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```
在Model的build.gradle中添加 [![](https://jitpack.io/v/ftmtshuashua/RequestChain.svg)](https://jitpack.io/#ftmtshuashua/RequestChain)
```
dependencies {
    implementation 'com.github.ftmtshuashua:RequestChain:version'
}
```
Java8 配置
```
再Model(app)下的build.gradle中配置
android{
  compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
 }
```


## LICENSE

```
Copyright (c) 2018-present, RequestChain Contributors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```




