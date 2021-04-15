#include <jni.h>
#include "../interop.hh"
#include "../shaper/interop.hh"
#include "../paragraph/interop.hh"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_2) != JNI_OK)
        return JNI_ERR;

    return JNI_VERSION_1_2;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_impl_Library__1nAfterLoad
  (JNIEnv* env, jclass jclass) {
    env->EnsureLocalCapacity(64);
    java::onLoad(env);
    skija::onLoad(env);
    skija::shaper::onLoad(env);
    skija::paragraph::onLoad(env);
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_2) != JNI_OK)
        return;

    skija::paragraph::onUnload(env);
    skija::shaper::onUnload(env);
    skija::onUnload(env);
    java::onUnload(env);
}
