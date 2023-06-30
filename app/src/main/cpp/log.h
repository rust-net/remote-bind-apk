//
// Created by ly on 2023/6/30.
//

#ifndef REMOTEBIND_LOG_H
#define REMOTEBIND_LOG_H

#include <android/log.h>
#define R(x) #x
#define STR(x) R(x)
#define LOG(...) __android_log_print(ANDROID_LOG_DEBUG, __FILE_NAME__ ":" STR(__LINE__), ##__VA_ARGS__)
#define TLOG(tag, fmt, ...) __android_log_print(ANDROID_LOG_DEBUG, __FILE_NAME__ ":" STR(__LINE__), "%s -> " fmt, tag, ##__VA_ARGS__)
#define FLOG(fmt, ...) TLOG(__func__, fmt, ##__VA_ARGS__)


#endif //REMOTEBIND_LOG_H
