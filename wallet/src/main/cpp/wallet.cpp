#include <jni.h>
#include <string>
#include "wallet.h"
#include "jni_helper.h"


const int PWD_LEN = 128;

 extern "C" JNIEXPORT jstring JNICALL
Java_com_zml_wallet_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_zml_wallet_NativeLib_createWallet(JNIEnv *env, jobject thiz,  jstring pwd) {


    const char *cstr = env->GetStringUTFChars(pwd, NULL);
    std::string walletKey = TrustWallet::register_wallet(PWD_LEN,std::string(cstr));
    env->ReleaseStringUTFChars(pwd, cstr);

    return env->NewStringUTF(walletKey.c_str());
}



extern "C"
JNIEXPORT jstring JNICALL
Java_com_zml_wallet_NativeLib_importMnemonic(JNIEnv *env, jobject thiz, jstring mnemonic,
                                             jstring pwd) {
    const char *cstr = env->GetStringUTFChars(pwd, NULL);
    std::string c_pwd = std::string(cstr);

    const char *cstrMnemonic = env->GetStringUTFChars(mnemonic, NULL);
    std::string c_mnemonic = std::string(cstrMnemonic);

    std::string walletKey = TrustWallet::importMnemonic(c_mnemonic,c_pwd);
    env->ReleaseStringUTFChars(pwd, cstr);
    env->ReleaseStringUTFChars(mnemonic, cstrMnemonic);

    return env->NewStringUTF(walletKey.c_str());
}
//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_zml_wallet_NativeLib_getWalletPrivateKey(JNIEnv *env, jobject thiz, jstring wallet_key,
//                                                  jstring pwd) {
//    const char *cstr = env->GetStringUTFChars(pwd, NULL);
//    std::string c_pwd = std::string(cstr);
//
//    const char *cstr_wallet_key = env->GetStringUTFChars(wallet_key, NULL);
//    std::string c_walletKey = std::string(cstr_wallet_key);
//
//    auto walletImpl = TrustWallet::getWalletWithKey(c_walletKey,c_pwd);
//
//    env->ReleaseStringUTFChars(pwd, cstr);
//    env->ReleaseStringUTFChars(wallet_key, cstr_wallet_key);
//    if (walletImpl == nullptr){
//        return env->NewStringUTF("");
//    }
//
//    auto privateKey = TW_WRAP(TWPrivateKey, TWHDWalletGetMasterKey(walletImpl.get(),TWCurveSECP256k1));
//    auto publicKey = TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeySecp256k1(privateKey.get(), true));
//    auto tw_data = TW_WRAPD(TWPublicKeyData(publicKey.get()));
//    auto tw_string = TW_WRAPS(TWStringCreateWithHexData(tw_data.get()));
//
//    return env->NewStringUTF(TWStringUTF8Bytes(tw_string.get()));
//
//}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_zml_wallet_NativeLib_getPublicChainPrivateKey(JNIEnv *env, jobject thiz,
                                                       jstring wallet_key, jstring pwd,
                                                       jint coin_type) {
    const char *cstr = env->GetStringUTFChars(pwd, NULL);
    std::string c_pwd = std::string(cstr);

    const char *cstr_wallet_key = env->GetStringUTFChars(wallet_key, NULL);
    std::string c_walletKey = std::string(cstr_wallet_key);
    TWCoinType coinType = static_cast<TWCoinType>(coin_type);

    auto privateKey = TrustWallet::getPrivateKey(c_walletKey,c_pwd,coinType);
    env->ReleaseStringUTFChars(pwd, cstr);
    env->ReleaseStringUTFChars(wallet_key, cstr_wallet_key);
    if (privateKey == nullptr){
        return env->NewStringUTF("");
    }
    auto tw_string = TW_WRAPS(TWStringCreateWithHexData(TW_WRAPD(TWPrivateKeyData(privateKey.get())).get()));
    return env->NewStringUTF(TWStringUTF8Bytes(tw_string.get()));

}
extern "C"
JNIEXPORT void JNICALL
Java_com_zml_wallet_NativeLib__1init(JNIEnv *env, jobject thiz) {
    tools_storage::init();
}



extern "C"
{

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    JniHelper::setJavaVM(vm);

    return JNI_VERSION_1_4;
}

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_zml_wallet_NativeLib_getPublicChainAddress(JNIEnv *env, jobject thiz, jstring wallet_key,
                                                    jstring pwd, jint coin_type) {


    const char *cstr = env->GetStringUTFChars(pwd, NULL);
    std::string c_pwd = std::string(cstr);

    const char *cstr_wallet_key = env->GetStringUTFChars(wallet_key, NULL);
    std::string c_walletKey = std::string(cstr_wallet_key);
    TWCoinType coinType = static_cast<TWCoinType>(coin_type);

    auto privateKey = TrustWallet::getPrivateKey(c_walletKey,c_pwd,coinType);
    env->ReleaseStringUTFChars(pwd, cstr);
    env->ReleaseStringUTFChars(wallet_key, cstr_wallet_key);
    if (privateKey == nullptr){
        return env->NewStringUTF("");
    }
    auto tw_string = TW_WRAPS(TWCoinTypeDeriveAddress(coinType,privateKey.get()));

    return env->NewStringUTF(TWStringUTF8Bytes(tw_string.get()));

}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_zml_wallet_NativeLib_getPublicChainPublicKey(JNIEnv *env, jobject thiz, jstring wallet_key,
                                                      jstring pwd, jint coin_type) {

    const char *cstr = env->GetStringUTFChars(pwd, NULL);
    std::string c_pwd = std::string(cstr);

    const char *cstr_wallet_key = env->GetStringUTFChars(wallet_key, NULL);
    std::string c_walletKey = std::string(cstr_wallet_key);
    TWCoinType coinType = static_cast<TWCoinType>(coin_type);


    env->ReleaseStringUTFChars(pwd, cstr);
    env->ReleaseStringUTFChars(wallet_key, cstr_wallet_key);


    jclass class_hashmap;
    class_hashmap = env->FindClass( "java/util/HashMap");
    jmethodID hashmap_init = env->GetMethodID(class_hashmap, "<init>",
                                                 "()V");
    jobject obj_hashmap = env->NewObject( class_hashmap, hashmap_init);
    jmethodID HashMap_put = env->GetMethodID(class_hashmap, "put",
                                                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    auto map_insert = [&obj_hashmap,&HashMap_put,&env](const std::string& key, std::shared_ptr<TWPublicKey>& publicKey){
        auto tw_data = TW_WRAPD(TWPublicKeyData(publicKey.get()));
        auto tw_string = TW_WRAPS(TWStringCreateWithHexData(tw_data.get()));

        env->CallObjectMethod( obj_hashmap, HashMap_put, env->NewStringUTF( key.c_str()),
                               env->NewStringUTF( TWStringUTF8Bytes(tw_string.get())));

    };

    {
        auto privateKey = TrustWallet::getPrivateKey(c_walletKey, c_pwd, coinType);
        if (privateKey == nullptr)
            return env->NewStringUTF("");
        auto publicKey = TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeySecp256k1(privateKey.get(), true));
        map_insert("secp256k1", publicKey);

    }
    {
        auto privateKey = TrustWallet::getPrivateKey(c_walletKey, c_pwd, coinType);
        auto publicKey =  TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeyEd25519(privateKey.get()));
        map_insert("ed25519", publicKey);
    }
    {
        auto privateKey = TrustWallet::getPrivateKey(c_walletKey, c_pwd, coinType);
        auto publicKey =  TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeyEd25519Blake2b(privateKey.get()));
        map_insert("ed25519-blake2b-nano", publicKey);
    }
    {
        auto privateKey = TrustWallet::getPrivateKey(c_walletKey, c_pwd, coinType);
        auto publicKey =  TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeyCurve25519(privateKey.get()));
        map_insert("curve25519", publicKey);
    }
    {
        auto privateKey = TrustWallet::getPrivateKey(c_walletKey, c_pwd, coinType);
        auto publicKey =  TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeyNist256p1(privateKey.get()));
        map_insert("nist256p1", publicKey);
    }

    return obj_hashmap;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_zml_wallet_NativeLib_importPrivateKey(JNIEnv *env, jobject thiz, jstring private_key,
                                               jint coin_type) {

    const char *cpk = env->GetStringUTFChars(private_key, NULL);
    std::string c_privateKey = std::string(cpk);

    TWCoinType coinType = static_cast<TWCoinType>(coin_type);

    std::string ret =TrustWallet::importPrivateKey(coinType, c_privateKey);
    return env->NewStringUTF(ret.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_zml_wallet_NativeLib_getMnemonic(JNIEnv *env, jobject thiz, jstring wallet_key,
                                          jstring pwd) {
    const char *cstr = env->GetStringUTFChars(pwd, NULL);
    std::string c_pwd = std::string(cstr);

    const char *cstr_wallet_key = env->GetStringUTFChars(wallet_key, NULL);
    std::string c_walletKey = std::string(cstr_wallet_key);

    auto mnemonic = TrustWallet::loadMnemonic(c_walletKey);
    env->ReleaseStringUTFChars(pwd, cstr);
    env->ReleaseStringUTFChars(wallet_key, cstr_wallet_key);

    return env->NewStringUTF((mnemonic.c_str()));
}