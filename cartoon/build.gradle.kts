import june.wing.GroupIdMavenCentral
import june.wing.beijingTimeVersion
import june.wing.publishAndroidMavenCentral

plugins {
    id("com.android.library")
    alias(vcl.plugins.gene.android)
}

android {
    namespace = "osp.spark.cartoon"
}

group = GroupIdMavenCentral
version = beijingTimeVersion
publishAndroidMavenCentral("cartoon")

dependencies {
//    https://developer.android.google.cn/develop/ui/views/animations/spring-animation?hl=en
//    https://developer.android.google.cn/jetpack/androidx/releases/dynamicanimation
    implementation(vcl.androidx.dynamicanimation.ktx)
    implementation(vcl.androidx.transition.ktx)
    val composeBom = platform(vcl.androidx.compose.bom)
    implementation(composeBom)
    implementation(vcl.androidx.compose.animation.core)
}