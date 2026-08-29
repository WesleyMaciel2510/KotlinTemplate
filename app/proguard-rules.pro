# Keep kotlinx.serialization serializers
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class *

# Keep navigation serialization
-keep class androidx.navigation.serialization.** { *; }

# Keep Kotlin metadata for serialization
-keep class kotlin.Metadata { *; }