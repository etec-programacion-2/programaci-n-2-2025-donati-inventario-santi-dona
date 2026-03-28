/*
 * Configuración del proyecto Kotlin con Gradle
 * Proyecto: Gestor de Inventario con GUI estilo Libertya (Swing)
 * Funcionalidades: gestión de stock, usuarios y exportación a Excel.
 */

plugins {
    // Kotlin para JVM
    alias(libs.plugins.kotlin.jvm)

    // Plugin para ejecutar la aplicación
    application
}

repositories {
    // Repositorio principal
    mavenCentral()
}

dependencies {
    // Librería estándar de Kotlin
    implementation(kotlin("stdlib"))

    // Librería Guava (útil para utilidades varias)
    implementation(libs.guava)

    // Apache POI para exportar a Excel (.xls y .xlsx)
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // FlatLaf: para darle un estilo moderno (tipo Libertya azul)
    implementation("com.formdev:flatlaf:3.4")

    // Gson: para guardar y leer datos en JSON (usuarios, inventario, etc.)
    implementation("com.google.code.gson:gson:2.11.0")

    // JUnit 5 para pruebas unitarias
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Configuración del compilador Kotlin (DSL moderno)
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

// Configuración del JDK
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Clase principal del programa (App.kt)
    mainClass = "org.example.AppKt"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// Permitir entrada por consola
tasks.withType<JavaExec> {
    standardInput = System.`in`
}





