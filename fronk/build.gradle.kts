import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "2.1.8.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

group = "com.fynes"
version = "1.0"

repositories {
    mavenCentral()
}

var arrow_version = "0.9.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation(project(":kronk"))
    implementation("io.arrow-kt:arrow-core-data:$arrow_version")
    implementation("io.arrow-kt:arrow-core-extensions:$arrow_version")
    implementation("io.arrow-kt:arrow-syntax:$arrow_version")
    implementation("io.arrow-kt:arrow-typeclasses:$arrow_version")
    implementation("io.arrow-kt:arrow-extras-data:$arrow_version")
    implementation("io.arrow-kt:arrow-extras-extensions:$arrow_version")
    implementation("io.arrow-kt:arrow-meta:$arrow_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
