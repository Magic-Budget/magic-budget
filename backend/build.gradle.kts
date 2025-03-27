import org.checkerframework.gradle.plugin.CheckerFrameworkExtension

plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.checkerframework") version "0.6.52"
}

group = "me.magicbudget"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate.validator:hibernate-validator")
    implementation("org.postgresql:postgresql:42.7.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:2.3.232")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("net.sourceforge.tess4j:tess4j:5.15.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}



configure<CheckerFrameworkExtension> {
    checkers = listOf(
            "org.checkerframework.checker.tainting.TaintingChecker",
            "org.checkerframework.checker.sqlquotes.SqlQuotesChecker",
    )
}
