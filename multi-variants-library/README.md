### Welcome to follow me on GitHub or CSDN

GitHub: https://github.com/JackyAndroid

CSDN: http://blog.csdn.net/rain_butterfly

[中文版文档](https://github.com/JackyAndroid/Android-Architecture-Fairy/blob/master/multi-variants-library/README-CN.md)

---

# Modular and componentization - more modules to distinguish the compilation

Sometimes we have multiple product forms in a project, and different products require a different compilation environment, which is the basis of modular component. The most common situation is in the main module which joined the channel statistics, but this time if we have multiple libraries, a variety of product forms of the main module need to compile a variety of product form of the Library, how to do?Look at the [official documents](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Library-Publication) below

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

The default library only releases the Release version, which is the official limitation of Gradle, and Google is trying to solve the problem. You can use defaultPublishConfig to set the published version by setting publishNonDefault true to let the library publish multiple product versions.

The usual way this is usually written depends on the default release version:

```java
dependencies {
    compile project(':libraries:lib2')
}
```

If you want to compile a variety of forms of library, you need to modify the following way:

```java
dependencies {
    flavor1Compile project(path: ':lib1', configuration: 'flavor1Release')
    flavor2Compile project(path: ':lib1', configuration: 'flavor2Release')
}
```

------

**Precautions**

1. The compiled configuration is the full name of the product name plus the compiled type, such as: flavor1Release
2. If Library is Maven's public library, it is best not to use this feature
3. The first letter of the product name to lowercase, otherwise there will be grammatical problems
4. If Gradle inside the use of MultiDex option will generate multiple aar



