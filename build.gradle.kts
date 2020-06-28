import org.gradle.jvm.tasks.Jar

plugins {
    `maven-publish`
    kotlin("jvm") version "1.3.71"
    id("org.jetbrains.dokka") version "0.10.0"
}

group = "com.github.kaning"
version = "0.0.8"

val jodamoneyVersion: String by project

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fatboyindustrial.gson-jodatime-serialisers:gson-jodatime-serialisers:1.7.0")
    implementation("org.joda:joda-money:$jodamoneyVersion")
    testImplementation("junit:junit:4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(tasks.dokka)
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
            artifact(dokkaJar)
        }
    }
    repositories {
        mavenLocal()
        maven {
            url = uri("$buildDir/repository")
        }
    }
}
