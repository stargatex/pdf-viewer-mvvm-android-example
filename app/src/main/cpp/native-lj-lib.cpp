#include <jni.h>
#include <string>
//
// Created by LahiruJaya on 21/01/2020.
//


extern "C"
JNIEXPORT jstring JNICALL
Java_com_stargatex_lahiru_basicpdfviwer_utils_constant_SecConstants_devServiceBaseUrlJNI(JNIEnv *env, jclass type) {
    return env->NewStringUTF("<base url>");

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_stargatex_lahiru_basicpdfviwer_utils_constant_SecConstants_devOauthClientId(JNIEnv *env, jclass type) {
    return env->NewStringUTF("<client id>");
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_stargatex_lahiru_basicpdfviwer_utils_constant_SecConstants_devOauthClientSecret(JNIEnv *env, jclass type) {
    return env->NewStringUTF("<client secret>");
}

//TODO: Configure other ENV

//store
extern "C"
JNIEXPORT jstring JNICALL
Java_com_stargatex_lahiru_basicpdfviwer_utils_constant_SecConstants_trustAccessKeyJNI(JNIEnv *env, jclass type) {
    return env->NewStringUTF("<bks pwd>");
}


