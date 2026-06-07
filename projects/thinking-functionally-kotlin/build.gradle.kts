plugins {
    kotlin("jvm") version "2.4.0"
}

group = "com.evolutionnext"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core:2.2.3")
    implementation("io.arrow-kt:arrow-fx-coroutines:2.2.3")

    testImplementation("io.kotest:kotest-runner-junit5:6.1.11")
    testImplementation("io.kotest:kotest-assertions-core:6.1.11")
}

kotlin {
    jvmToolchain(26)
}

tasks.test {
    useJUnitPlatform()
}
