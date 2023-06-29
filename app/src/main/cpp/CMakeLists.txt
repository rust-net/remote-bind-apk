# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("remote_bind")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        remote_bind

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        bridge.cpp
        )

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

set(jniLibs ${CMAKE_SOURCE_DIR}/../jniLibs)
set(CMAKE_FIND_ROOT_PATH ${jniLibs}/${CMAKE_ANDROID_ARCH_ABI})
set(CMAKE_FIND_LIBRARY_CUSTOM_LIB_SUFFIX ../) # 默认查找搜索路径下的 lib 目录, 为了删除该目录, 设置该前缀
find_library(
        android-lib
        NAMES rust
)
message("android-lib: ${android-lib}") # Expect: ...app/src/main/cpp/../jniLibs/arm64-v8a/Windows/../librust.so

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        remote_bind

        ${android-lib}

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})