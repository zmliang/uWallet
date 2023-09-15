# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.

project("wallet")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        wallet

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        #include/TrustWalletCore/*.h
        sqlite3/sqlite3.h
        sqlite3/sqlite3.c
        sqlite3/sqlite3ext.h

        local_storage.cpp
        local_storage.h

        wallet.cpp
        wallet.h
        log_util.h
        jni_helper.cpp
        jni_helper.h
       )


# 引入静态库
#       ① 参数 1 ( add ) : 设置引入的静态库名称
#       ② 参数 2 ( SHARED ) : 设置引入的函数库类型 : ① 静态库 STATIC ② 动态库 SHARED
#       ③ 参数 3 ( IMPORTED ) : 表示引入第三方静态库 , 导入静态库 , 相当于预编译静态库
#                                   后续还需要设置导入路径 , 配合该配置使用
add_library(
        # 设置引入的静态库名称
        TrustWalletCore

        # 设置引入的函数库类型为静态库
        STATIC

        # 表示引入第三方静态库
        IMPORTED)

# 设置上述静态库的导入路径
#       设置目标属性参数 :
#           ① 参数 1 ( add ) : 要设置哪个函数库的属性
#           ② 参数 2 ( PROPERTIES ) : 设置目标属性
#           ③ 参数 3 ( IMPORTED_LOCATION ) : 设置导入路径
#           ④ 参数 4 : 配置静态库的文件路径
set_target_properties(
        # 设置目标
        TrustWalletCore

        # 设置属性
        PROPERTIES

        # 导入路径
        IMPORTED_LOCATION

        # ${CMAKE_SOURCE_DIR} 是本 CMakeList.txt 构建脚本的路径 , 是 CMake 工具内置的变量
        #       Android CMake 也内置了一些变量 , 如 ANDROID_ABI
        ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libTrustWalletCore.a)

# 打印日志信息
#       ${ANDROID_ABI} 的作用是获取当前的 CPU 指令集架构
#           当本次编译 armeabi-v7a CPU 架构时 , ${ANDROID_ABI} 值为 armeabi-v7a
#           当本次编译 x86 CPU 架构时 , ${ANDROID_ABI} 值为 x86
message("CMAKE_SOURCE_DIR : ${CMAKE_SOURCE_DIR}, ANDROID_ABI : ${ANDROID_ABI}")

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        wallet

        TrustWalletCore
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})