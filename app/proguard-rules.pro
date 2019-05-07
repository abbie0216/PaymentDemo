# Preverify is useless in Android
-dontpreverify

-keepattributes *Annotation*,InnerClasses,EnclosingMethod
-keepattributes Signature

# Keep code line number
-keepattributes SourceFile,LineNumberTable

#############################################
#
# Android
#
#############################################
# Keep 4 main components
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

-keep class **.R$* {*;}

-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#############################################
#
# Third party library
#
#############################################
# Gson
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Keep object created by Gson
-keep class com.idhub.shop.model.** {*;}

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

-dontwarn org.w3c.dom.bootstrap.**

-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.**

# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**

-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn org.slf4j.**

-keep class * extends org.web3j.abi.TypeReference
-keep class * extends org.web3j.abi.datatypes.Type
-keep class org.web3j.** {*;}

-keepclassmembers class org.web3j.protocol.** { *; }
-keepclassmembers class org.web3j.crypto.* { *; }

-keep class org.bouncycastle.**
-dontwarn org.bouncycastle.jce.provider.X509LDAPCertStoreSpi
-dontwarn org.bouncycastle.x509.util.LDAPStoreHelper

-dontwarn jnr.posix.**

-dontwarn java8.util.**
