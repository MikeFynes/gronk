import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.fynes"
version = "1.0"

repositories {
    mavenCentral()
}

var arrow_version = "0.9.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api

    implementation("io.arrow-kt:arrow-core-data:$arrow_version")
    implementation("io.arrow-kt:arrow-core-extensions:$arrow_version")
    implementation("io.arrow-kt:arrow-syntax:$arrow_version")
    implementation("io.arrow-kt:arrow-typeclasses:$arrow_version")
    implementation("io.arrow-kt:arrow-extras-data:$arrow_version")
    implementation("io.arrow-kt:arrow-extras-extensions:$arrow_version")
    implementation("io.arrow-kt:arrow-meta:$arrow_version")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")

    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.2")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
