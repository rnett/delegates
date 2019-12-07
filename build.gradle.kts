plugins {
    kotlin("multiplatform") version "1.3.60"
    maven
    `maven-publish`
}

//group = "com.rnett.delegates"
//version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

kotlin {

    jvm() {

    }

    js() {

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

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }

    }
}

//publishing.publications.all {
//    println("Version: ${project.version}, ${version}")
//    version = project.version.toString()
//}

//publishing {
//    publications{
//        create("default", MavenPublication::class) {
//            from(components["kotlin"])
//            group = project.group
//            artifactId = project.name
////            version = project.version.toString()
//        }
//    }
//}