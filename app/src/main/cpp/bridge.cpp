#include <jni.h>
#include <string>
#include "rust.h"
#include "log.h"

extern "C" JNIEXPORT void JNICALL
Java_app_remote_1bind_Bridge_test(
        JNIEnv* env,
        jobject /* this */) {
    test();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_app_remote_1bind_Bridge_start(JNIEnv *env, jobject thiz, jstring i_server, jshort i_port,
                                   jstring i_password, jstring i_local_service) {
    auto port = (uint16_t) i_port;
    const char *server = env->GetStringUTFChars(i_server, 0);
    const char *password = env->GetStringUTFChars(i_password, 0);
    const char *local_service = env->GetStringUTFChars(i_local_service, 0);
    // 在此处使用本地字符串
    const char *handler = start(server, port, password, local_service);
    // 释放字符串内存
    env->ReleaseStringUTFChars(i_server, server);
    env->ReleaseStringUTFChars(i_password, password);
    env->ReleaseStringUTFChars(i_local_service, local_service);
    // 返回任务句柄
    return env->NewStringUTF(handler);
}

extern "C"
JNIEXPORT void JNICALL
Java_app_remote_1bind_Bridge_stop(JNIEnv *env, jobject thiz, jstring i_handler) {
    const char *handler = env->GetStringUTFChars(i_handler, 0);
    stop(handler);
    // 释放字符串内存
    env->ReleaseStringUTFChars(i_handler, handler);
}