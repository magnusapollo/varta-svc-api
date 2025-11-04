import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.diffplug.spotless") version "6.25.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories { mavenCentral() }

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.commonmark:commonmark:0.21.0")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    testCompileOnly("org.projectlombok:lombok:1.18.42")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging { exceptionFormat = TestExceptionFormat.FULL }
}

spotless {
    java { googleJavaFormat() }
    format("misc") {
        target("**/*.md", "**/.gitignore", "**/*.yml", "**/*.json")
        prettier().config(mapOf("printWidth" to 100))
    }
}

// Convenience tasks
tasks.register("printSwagger") {
    dependsOn("bootRun")
}