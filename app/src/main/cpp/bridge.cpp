#include <jni.h>
#include <string>

extern "C" void test();

extern "C" JNIEXPORT void JNICALL
Java_app_remote_1bind_Bridge_test(
        JNIEnv* env,
        jobject /* this */) {
    test();
}