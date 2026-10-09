plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "submarino"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

val customMainClass = project.findProperty("mainClass") as String? ?: "submarino.MainKt"
application {
    mainClass.set(customMainClass)
}

tasks.register<JavaExec>("runIssue5") {
    group = "application"
    description = "Ejecuta la prueba de la Issue #5: Minerales y Recolección"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("submarino.MainPruebaIssue5Kt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}