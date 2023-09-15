//
// Created by 索二爷 on 2023/8/26.
//

#include "local_storage.h"
#include "log_util.h"
#include "jni_helper.h"

static int _initialized=0;

 void localStorageInit(const char *fullpath)
{
    if( ! _initialized )
    {
        _initialized = 1;
    }
}

void localStorageFree()
{
    if( _initialized ) {

        JniMethodInfo t;

        if (JniHelper::getStaticMethodInfo(t, "com/zml/wallet/WalletDb", "destory", "()V"))
        {
            t.env->CallStaticVoidMethod(t.classID, t.methodID);
            t.env->DeleteLocalRef(t.classID);
        }

        _initialized = 0;
    }
}


void localStorageSetItem( const char *key, const char *value)
{
    assert( _initialized );

    JniMethodInfo t;

    if (JniHelper::getStaticMethodInfo(t, "com/zml/wallet/WalletDb", "setItem", "(Ljava/lang/String;Ljava/lang/String;)V")) {
        jstring jkey = t.env->NewStringUTF(key);
        jstring jvalue = t.env->NewStringUTF(value);
        t.env->CallStaticVoidMethod(t.classID, t.methodID, jkey, jvalue);
        t.env->DeleteLocalRef(jkey);
        t.env->DeleteLocalRef(jvalue);
        t.env->DeleteLocalRef(t.classID);
    }
}

const char* localStorageGetItem( const char *key )
{
    assert( _initialized );
    JniMethodInfo t;
    if (JniHelper::getStaticMethodInfo(t, "com/zml/wallet/WalletDb", "getItem", "(Ljava/lang/String;)Ljava/lang/String;")) {
        jstring jkey = t.env->NewStringUTF(key);
        jstring ret = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID, jkey);
        const char* b = t.env->GetStringUTFChars( ret , 0 );
        t.env->DeleteLocalRef(ret);
        t.env->DeleteLocalRef(jkey);
        t.env->DeleteLocalRef(t.classID);
        return b ? b : NULL;
    }

    return NULL;
}

/** removes an item from the LS */
void localStorageRemoveItem( const char *key )
{
    assert( _initialized );
    JniMethodInfo t;

    if (JniHelper::getStaticMethodInfo(t, "com/zml/wallet/WalletDb", "removeItem", "(Ljava/lang/String;)V")) {
        jstring jkey = t.env->NewStringUTF(key);
        t.env->CallStaticVoidMethod(t.classID, t.methodID, jkey);
        t.env->DeleteLocalRef(jkey);
        t.env->DeleteLocalRef(t.classID);
    }

}


namespace tools_storage {

    void init()
    {
        localStorageInit("/storage/.wallet.db");
    }

    const std::string& encryption(const std::string& str)
    {
        if (str.empty())
            return str;

        return str;
    }

    const std::string& ecrypt(const std::string& str)
    {
        if (str.empty())
            return str;

        return str;
    }

    void save(const std::string& k, const std::string& v)
    {
        std::string key = encryption(k);
        std::string value = encryption(v);
        if (!key.empty() && !value.empty())
        {
            localStorageRemoveItem(key.c_str());
            localStorageSetItem(key.c_str(), value.c_str());
        }
    }

    std::string load(const std::string& k)
    {
        std::string key = encryption(k);
        std::string value;
        if (!key.empty())
        {
            const char* tmp = localStorageGetItem(key.c_str());
            const std::string v = (tmp != nullptr) ? tmp : "";
            value = ecrypt(v);
        }
        return value;
    }

    void remove(const std::string& k)
    {
        std::string key = encryption(k);
        if (!key.empty())
        {
            localStorageRemoveItem(key.c_str());
        }
    }

}