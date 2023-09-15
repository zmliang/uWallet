//
// Created by 索二爷 on 2023/9/5.
//

#ifndef JETPACK_TEST_LOG_UTIL_H
#define JETPACK_TEST_LOG_UTIL_H
#include <android/log.h>

#define TAG "native_wallet"

#define log_i(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO, TAG, FORMAT, ##__VA_ARGS__)
#define log_e(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR, TAG, FORMAT, ##__VA_ARGS__)


#endif //JETPACK_TEST_LOG_UTIL_H
