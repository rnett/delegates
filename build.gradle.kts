import java.net.URL

plugins {
    kotlin("multiplatform") version "1.3.60"
    maven
    `maven-publish`
}

fun getNewestCommit(gitURL: String): String {
    try {

        return URL("https://api.github.com/repos/$gitURL/commits?client_id=f98835efcec776b42a9c&client_secret=a73806fc946ced540c208be4320839ecb61c65d5").readText()
            .substringAfter("\"sha\":\"").substringBefore("\",").substring(0, 10)
    } catch (e: java.lang.Exception) {
        throw RuntimeException("Can't get the latest commit", e)
    }
}

group = "com.rnett.delegates"
version = "1.0-SNAPSHOT"
val do_jitpack_commit_fix = true

val latest_commit_version = getNewestCommit("rnett/" + project.group.toString().split(".").last())

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

val do_jitpack_fix = do_jitpack_commit_fix// && "jitpack" in projectDir.path

if (do_jitpack_fix) {
    tasks["publishToMavenLocal"].doLast {
        val artifacts = publishing.publications.filterIsInstance<MavenPublication>().map { it.artifactId }

        val dir: File = File(publishing.repositories.mavenLocal().url)
            .resolve(project.group.toString().replace('.', '/'))

        dir.listFiles { it -> it.name in artifacts }
            .flatMap { it.listFiles { it -> it.isDirectory }.toList() }
            .flatMap { it.listFiles { it -> it.name.endsWith(".module") }.toList() }
            .forEach {
                val text = it.readText()
                println("For $it, replacing ${project.version.toString()} with $latest_commit_version")
                it.writeText(text.replace(project.version.toString(), latest_commit_version))
            }
    }
}