[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)

[![Build](https://github.com/ArtemGet/entrys/actions/workflows/maven.yaml/badge.svg)](https://github.com/ArtemGet/entrys/actions/workflows/maven.yaml)
[![](https://jitpack.io/v/ArtemGet/entrys.svg)](https://jitpack.io/#ArtemGet/entrys)

# Overview

Library designed for retrieving properties set at application startup. If you don't use Spring Framework/Spring Boot but
like its configuration via yaml - you will find this library useful.

# Advantages

1) Lightweight, no need to pull lots of dependencies
2) No reflection and typecasts
3) Null safe and runtime exception free

# Quick start

This library is distributed via [jitpack.io](https://jitpack.io/#ArtemGet/entrys)

# Supported entries

1) Properties passed via -D
2) Environment variables
3) Json strings 

Note that using json entries requires additional dependencies:

```xml

<dependency>
  <groupId>org.glassfish</groupId>
  <artifactId>jakarta.json</artifactId>
  <version>1.1.6</version>
</dependency>
```

4) Yaml files

Note that using yaml entries requires additional dependencies:

```xml
<dependency>
   <groupId>com.amihaiemil.web</groupId>
   <artifactId>eo-yaml</artifactId>
   <version>8.0.6</version>
</dependency>
```

# Examples

## Properties:

```java
String prop=new EProp("your_prop_passed_via_-D").value();
```

## Environment variables:

```java
String env=new EEnv("my_env").value()
```

## Json strings:

TODO add docs

## Yaml files:

1) Works similar to spring
   framework [@Value](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Value.html)
2) Supports string, number, arrays, float, boolean as value
3) Supports default values and env injections
4) Default yaml file config is placed under "src/main/resources/application.yaml", but is configurable

```yaml
sex: "male"
person:
  name: "kekus"
  languages: [ ru,en ]
  age: ${AGE_ENV:123}
```

#### Getting attribute:

```java
String sex=new EVal("sex").value();
```

#### Getting nested attribute:

```java
String name=new EVal("person.name").value();
```

#### Getting array

```java
List<String> languages=new ESplit(new EVal("person.languages")).value();
```

#### Getting env

```java
String age=new EVal("person.age").value();
```

1) If there is a default value and env is not present - will return default value
2) If there is a default value and env is present - will return value from env
3) If default value is not present - will return value from env
