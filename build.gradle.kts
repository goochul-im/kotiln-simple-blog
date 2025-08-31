plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.25"
}

group = "kotlin"
version = "0.0.1-SNAPSHOT"
description = "simple-blog"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

//extra["snippetsDir"] = file("build/generated-snippets")

val jjwtVersion = "0.12.5"

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
//    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
//    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // https://mvnrepository.com/artifact/io.github.serpro69/kotlin-faker
    implementation("io.github.serpro69:kotlin-faker:1.16.0")
    implementation("p6spy:p6spy:3.9.1")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.5.5")
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.5.5")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    // JJWT (Java JWT) 라이브러리
//    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
//    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
//    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

    implementation("com.auth0:java-jwt:4.4.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.named<Test>("test") {
//    outputs.dir(extra["snippetsDir"]!!)
    useJUnitPlatform()
}

//tasks.named("asciidoctor") {
//    inputs.dir(extra["snippetsDir"]!!)
//    dependsOn(tasks.named("test"))
//}
