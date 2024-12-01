plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.kr.assignment"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "localDependencies", "include" to listOf("*.jar"))))

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.26.3")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}