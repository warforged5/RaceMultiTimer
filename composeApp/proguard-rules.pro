# Keep Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.racetimer.app.**$$serializer { *; }
-keepclassmembers class com.racetimer.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.racetimer.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Compose runtime
-keep class androidx.compose.** { *; }

# Keep all public composables
-keepclassmembers class * {
    @androidx.compose.runtime.Composable public <methods>;
}