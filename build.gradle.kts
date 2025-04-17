plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "mcp"
version = "0.0.1-SNAPSHOT"

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.set(
            listOf(
                "-Xjsr305=strict",
                "-Xcontext-receivers"
            )
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.ai:spring-ai-mcp-server-spring-boot-starter:1.0.0-M6")
    implementation("io.github.classgraph:classgraph:4.8.179")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Task: generate manifest.json based on @Tool annotations
tasks.register<JavaExec>("generateManifest") {
    group = "build"
    description = "Generates manifest.json from @Tool annotations"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("mcp.datetime.ManifestGenerator")
}

// Task: copy the Spring Boot executable JAR to ./libs for publishing
tasks.register<Copy>("copyJarToLibs") {
    dependsOn(tasks.named("bootJar"))
    from(tasks.named<Jar>("bootJar").flatMap { it.archiveFile })
    into(layout.projectDirectory.dir("libs"))
}

// Ensure build also runs manifest generation and jar copy
tasks.named("build") {
    dependsOn("generateManifest")
    dependsOn("copyJarToLibs")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveVersion.set(version.toString())
    mainClass.set("mcp.datetime.McpDateTimeApplicationKt")
}
