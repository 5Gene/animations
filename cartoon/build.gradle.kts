import java.io.ByteArrayOutputStream

plugins {
    id("com.android.library")
    alias(wings.plugins.android)
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.28.0"
}

group = "io.github.5hmlA"
version = "1.0"

//apply from: file('../publish-plugin.gradle')

android {
    namespace = "osp.sparkj.cartoon"
    buildTypes {
        release {}
    }
//    publishing {
//        singleVariant("release") {
//            withSourcesJar()
//        }
//    }
}

dependencies {
//    https://developer.android.google.cn/develop/ui/views/animations/spring-animation?hl=en
//    https://developer.android.google.cn/jetpack/androidx/releases/dynamicanimation
    implementation("androidx.dynamicanimation:dynamicanimation-ktx:1.0.0-alpha03")
    implementation("androidx.dynamicanimation:dynamicanimation:1.1.0-alpha03")
    implementation("androidx.compose.animation:animation-core:1.6.7")
    implementation(libs.androidx.core.ktx)
    implementation("androidx.transition:transition-ktx:1.5.0")
}


val gitUrl: String by lazy {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "config", "--get", "remote.origin.url")
        standardOutput = stdout
    }
    val remoteUrl = stdout.toString().trim()
    println("Remote URL: ${remoteUrl.removeSuffix(".git")}")
    remoteUrl
}


println("xxxxxxxxxxx ${properties["mavenCentralUsername"]}")
println("xxxxxxxxxxx ${properties["mavenCentralPassword"]}")

mavenPublishing {
    coordinates("io.github.5hmla", "cartoon", "1.0")
}

publishing {
    publications {
        repositories {
            maven {
                name = "LocalTest"
                setUrl("repos")
            }
        }
        register<MavenPublication>("osp") {
            groupId = group.toString().lowercase()
//            artifactId = name
            version = "1.0"
            afterEvaluate {
                from(components["release"])
            }

            pom {
                description = "android animations library 🚀"
                url = gitUrl.removeSuffix(".git")
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id.set("5hmlA")
                        name.set("ZuYun")
                        email.set("jonsa.jzy@gmail.com")
                        url.set("https://github.com/5hmlA")
                    }
                }
                scm {
                    connection.set("scm:git:$gitUrl")
                    developerConnection.set("scm:git:ssh:${gitUrl.substring(6)}")
                    url.set(gitUrl.removeSuffix(".git"))
                }
            }
        }
    }
}
