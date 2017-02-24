# 模块化与组件化--多模块区分编译

> **示例地址：https://github.com/JackyAndroid/Android-Architecture-Fairy/tree/master/multi-variants-library**
> Android-Architecture-Fairy开源项目重点分析主流技术与架构设计，欢迎star

有时我们一个项目中存在多个产品形态，且不同产品需要不同的编译环境，这是模块化组件化的基础。最普通的情况便是在主模块里面加入渠道统计，但此时如果我们有多个Library，多种产品形态的主模块需要编译多种产品形态的Library，怎么办？先看下[官方文档](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Library-Publication)如下：

By default a library only publishes its release variant. This variant will be used by all projects referencing the library, no matter which variant they build themselves. This is a temporary limitation due to Gradle limitations that we are working towards removing. You can control which variant gets published:
```java
android {
    defaultPublishConfig "debug"
}
```
Note that this publishing configuration name references the full variant name. Release and debug are only applicable when there are no flavors. If you wanted to change the default published variant while using flavors, you would write:
```java
android {
    defaultPublishConfig "flavor1Debug"
}
```
It is also possible to publish all variants of a library. We are planning to allow this while using a normal project-to-project dependency (like shown above), but this is not possible right now due to limitations in Gradle (we are working toward fixing those as well).
Publishing of all variants are not enabled by default. The snippet below enables this feature:
```java
android {
    publishNonDefault true
}
```
It is important to realize that publishing multiple variants means publishing multiple aar files, instead of a single aar containing multiple variants. Each aar packaging contains a single variant. Publishing a variant means making this aar available as an output artifact of the Gradle project. This can then be used either when publishing to a maven repository, or when another project creates a dependency on the library project.

Gradle has a concept of default" artifact. This is the one that is used when writing:
```java
dependencies {
    compile project(':libraries:lib2')
}
```
To create a dependency on another published artifact, you need to specify which one to use:
```java
dependencies {
    flavor1Compile project(path: ':lib1', configuration: 'flavor1Release')
    flavor2Compile project(path: ':lib1', configuration: 'flavor2Release')
}
```
Important: Note that the published configuration is a full variant, including the build type, and needs to be referenced as such. 
Important: When enabling publishing of non default, the Maven publishing plugin will publish these additional variants as extra packages (with classifier). This means that this is not really compatible with publishing to a maven repository. You should either publish a single variant to a repository OR enable all config publishing for inter-project dependencies.

默认Library只发布Release版本，这个是Gradle官方的限制，Google官方正在试图解决这个问题。可以使用defaultPublishConfig去设置发布的版本，通过设置publishNonDefault true可以让Library发布多个产品版本。
下面的这种平常书写的方式是依赖的默认发布版本：
```java
dependencies {
    compile project(':libraries:lib2')
}
```
如果想要分渠道编译多种形态的Library，需要修改如下的方式：
```java
dependencies {
    flavor1Compile project(path: ':lib1', configuration: 'flavor1Release')
    flavor2Compile project(path: ':lib1', configuration: 'flavor2Release')
}
```


----------


**注意事项：**

 1. 编译的配置是产品名称的全称加编译类型，如：flavor1Release
 2. 如果Library是Maven的公开库最好不要使用这种特性
 3. 产品名称首字母要小写，否则会有语法问题
 4. 如果Gradle里面有使用MultiDex选项会生成多个aar