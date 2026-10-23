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

application {
    mainClass.set("submarino.Main")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runIssue5") {
    group = "application"
    description = "Ejecuta la prueba de la Issue #5: Minerales y Recolección"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("submarino.MainPruebaIssue5")
}

tasks.register<JavaExec>("runIssue6") {
    group = "application"
    description = "Ejecuta la prueba de la Issue #6: Cálculo de peso total y física de flotabilidad/ascenso"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("submarino.MainPruebaIssue6")
}

tasks.register<JavaExec>("runIssue7") {
    group = "application"
    description = "Ejecuta la prueba de la Issue #7: Zona de acople, venta de minerales y monedas"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("submarino.MainPruebaIssue7")
}

tasks.register<JavaExec>("runIssue8") {
    group = "application"
    description = "Ejecuta la prueba de la Issue #8: Sistema de upgrades y mejoras del submarino"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("submarino.MainPruebaIssue8")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
