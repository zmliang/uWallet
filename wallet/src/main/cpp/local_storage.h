//
// Created by 索二爷 on 2023/8/26.
//

#ifndef JETPACK_TEST_LOCAL_STORAGE_H
#define JETPACK_TEST_LOCAL_STORAGE_H


#include <assert.h>
#include "sqlite3/sqlite3.h"

#include <stdio.h>
#include <stdlib.h>
#include <string>


 void localStorageInit(const char *fullpath);

/** Frees the allocated resources */
 void localStorageFree();

/** sets an item in the LS */
 void localStorageSetItem(const char *key, const char *value);

/** gets an item from the LS */
 const char* localStorageGetItem(const char *key);

/** removes an item from the LS */
 void localStorageRemoveItem(const char *key);



namespace tools_storage {

    void init();

    void save(const std::string& k, const std::string& v);

    std::string load(const std::string& k);

    void remove(const std::string& k);
}

#endif //JETPACK_TEST_LOCAL_STORAGE_H
