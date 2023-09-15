//
// Created by 索二爷 on 2023/9/5.
//

#ifndef JETPACK_TEST_JNI_HELPER_H
#define JETPACK_TEST_JNI_HELPER_H

#include <jni.h>
#include <string>
#include "log_util.h"


typedef struct JniMethodInfo_
{
    JNIEnv *    env;
    jclass      classID;
    jmethodID   methodID;
} JniMethodInfo;

class  JniHelper{
public:
    static JavaVM* getJavaVM();
    static void setJavaVM(JavaVM *javaVM);
    static jclass getClassID(const char *className, JNIEnv *env=0);
    static bool getStaticMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode);
    static bool getMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode);
    static std::string jstring2string(jstring str);
    static void restartApp();

private:
    static JavaVM *m_psJavaVM;

};

#endif //JETPACK_TEST_JNI_HELPER_H
