plugins {
    kotlin("multiplatform") version "1.3.60"
    maven
    `maven-publish`
}

group = "com.rnett.delegates"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {

    jvm(){

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(kotlin("reflect"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }

    }
}

publishing {
    publications{
        create("default", MavenPublication::class) {
            from(components["kotlin"])
            group = "com.rnett.kframe"
            artifactId = "kframe"
            version = "1.0.0"
        }
    }
}