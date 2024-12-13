plugins {
    kotlin("jvm") version "2.0.0"
    application
    java
}

application {
    mainClass.set("org.kr.assignment.ReasonerRunner")
}

group = "org.kr.assignment"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "localDependencies", "include" to listOf("*.jar"))))
    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    // Include compiled classes and resources
    from(sourceSets.main.get().output)

    // Include dependencies
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}