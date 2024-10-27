import june.wing.GroupIdMavenCentral
import june.wing.publishAndroidMavenCentral

plugins {
    id("com.android.library")
    alias(vcl.plugins.gene.android)
}

group = GroupIdMavenCentral
version = libs.versions.gene.cartoon.get()

android {
    namespace = "osp.sparkj.cartoon"
}

publishAndroidMavenCentral("cartoon")

dependencies {
//    https://developer.android.google.cn/develop/ui/views/animations/spring-animation?hl=en
//    https://developer.android.google.cn/jetpack/androidx/releases/dynamicanimation
    implementation("androidx.dynamicanimation:dynamicanimation-ktx:1.0.0-alpha03")
    implementation("androidx.dynamicanimation:dynamicanimation:1.1.0-alpha03")
    implementation("androidx.compose.animation:animation-core:1.7.2")
    implementation("androidx.transition:transition-ktx:1.5.0")
}