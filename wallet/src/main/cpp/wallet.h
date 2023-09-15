//
// Created by 索二爷 on 2023/8/26.
//

#ifndef JETPACK_TEST_WALLET_H
#define JETPACK_TEST_WALLET_H


#include "include/TrustWalletCore/TWCoinType.h"
#include "include/TrustWalletCore/TWAnySigner.h"
#include "include/TrustWalletCore/TWCoinTypeConfiguration.h"
#include "include/TrustWalletCore/TWString.h"
#include "include/TrustWalletCore/TWData.h"
#include "include/TrustWalletCore/TWPrivateKey.h"
#include "include/TrustWalletCore/TWPublicKey.h"
#include "include/TrustWalletCore/TWStoredKey.h"
#include "include/TrustWalletCore/TWAccount.h"
#include "include/TrustWalletCore/TWHDWallet.h"
#include "include/TrustWalletCore/TWMnemonicLanguage.h"
#include "include/TrustWalletCore/TWHash.h"
#include "include/TrustWalletCore/TWBase64.h"

#include "log_util.h"


#include "local_storage.h"


#define TW_WRAP(type, x) std::shared_ptr<type>(x, type##Delete)
#define TW_WRAPD(x) std::shared_ptr<TWData>(x, TWDataDelete)
#define TW_WRAPS(x) std::shared_ptr<TWString>(x, TWStringDelete)
#define TW_STRING(x) std::shared_ptr<TWString>(TWStringCreateWithUTF8Bytes(x), TWStringDelete)
#define TW_DATA(x) std::shared_ptr<TWData>(TWDataCreateWithHexString(TW_STRING(x).get()), TWDataDelete)


namespace BaseWallet
{

    static std::string WalletTypeMnemonic = "Mnemonic";
    static std::string WalletTypePrivateKey = "PrivateKey";

    std::string load_wallet_key(const std::string& mnemonic, const std::string& password)
    {
        auto walletImp = TW_WRAP(TWHDWallet, TWHDWalletCreateWithMnemonic(TW_STRING(mnemonic.c_str()).get(), TW_STRING(password.c_str()).get()));
        auto privateKey = TW_WRAP(TWPrivateKey, TWHDWalletGetMasterKey(walletImp.get(), TWCurveSECP256k1));
        auto publicKey =  TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeySecp256k1(privateKey.get(), true));
        auto tw_data = TW_WRAPD(TWPublicKeyData(publicKey.get()));
        auto tw_string = TW_WRAPS(TWStringCreateWithHexData(tw_data.get()));
        std::string strPublicKey = TWStringUTF8Bytes(tw_string.get());
        return strPublicKey;
    }

    std::string load_wallet_key(const std::string& private_key)
    {
        auto privateKey = TW_WRAP(TWPrivateKey, TWPrivateKeyCreateWithData(TW_DATA(private_key.c_str()).get()));
        auto publicKey =  TW_WRAP(TWPublicKey, TWPrivateKeyGetPublicKeySecp256k1(privateKey.get(), true));
        auto tw_data = TW_WRAPD(TWPublicKeyData(publicKey.get()));
        auto tw_string = TW_WRAPS(TWStringCreateWithHexData(tw_data.get()));
        std::string strPublicKey = TWStringUTF8Bytes(tw_string.get());
        return strPublicKey;
    }

    std::string load_wallet_type(const std::string& wallet_key)
    {
        return tools_storage::load("TYPE:"+wallet_key);
    }

    std::string load_wallet(const std::string& wallet_key)
    {
        return tools_storage::load(wallet_key);
    }

    std::string save_wallet(const std::string& mnemonic, const std::string& password)
    {
        std::string wallet_key = load_wallet_key(mnemonic, password);
        tools_storage::save("TYPE:"+wallet_key, WalletTypeMnemonic);
        tools_storage::save(wallet_key, mnemonic);
        return wallet_key;
    }

    std::string save_wallet(const std::string& private_key)
    {
        std::string wallet_key = load_wallet_key(private_key);
        tools_storage::save("TYPE:"+wallet_key, WalletTypePrivateKey);
        tools_storage::save(wallet_key, private_key);
        return wallet_key;
    }

    void remove_wallet(const std::string& wallet_key)
    {
        tools_storage::remove(wallet_key);
        tools_storage::remove("TYPE:"+wallet_key);
    }
}


namespace TrustWallet
{

    std::string loadMnemonic(const std::string wallet_key)
    {
        if (BaseWallet::WalletTypePrivateKey == BaseWallet::load_wallet_type(wallet_key))
            return "";
        return BaseWallet::load_wallet(wallet_key);
    }

    std::string loadPrivateKey(const std::string wallet_key)
    {
        if (BaseWallet::WalletTypeMnemonic == BaseWallet::load_wallet_type(wallet_key))
            return "";
        return BaseWallet::load_wallet(wallet_key);
    }

    void removeWallet(const std::string wallet_key)
    {
        BaseWallet::remove_wallet(wallet_key);
    }

    std::shared_ptr<TWHDWallet> getWallet(const std::string& mnemonic, const std::string& password)
    {
        auto tw_mnemonic = TW_STRING(mnemonic.c_str());
        auto tw_password = TW_STRING(password.c_str());
        return TW_WRAP(TWHDWallet, TWHDWalletCreateWithMnemonic(tw_mnemonic.get(), tw_password.get()));
    }

    std::shared_ptr<TWHDWallet> getWalletWithKey(const std::string& wallet_key, const std::string& password)
    {
        std::string mnemonic = loadMnemonic(wallet_key);
        if (mnemonic.empty())
            return nullptr;
        return TrustWallet::getWallet(mnemonic, password);
    }

    std::shared_ptr<TWPrivateKey> getPrivateKey(const std::string& wallet_key, const std::string& password, TWCoinType coinType)
    {
        std::string type = BaseWallet::load_wallet_type(wallet_key);
        std::shared_ptr<TWPrivateKey> privateKey = nullptr;

        if (type == BaseWallet::WalletTypeMnemonic)
        {
            std::string mnemonic = loadMnemonic(wallet_key);
            if (!mnemonic.empty())
            {
                auto walletImp =  TrustWallet::getWallet(mnemonic, password);
                privateKey = TW_WRAP(TWPrivateKey, TWHDWalletGetKeyForCoin(walletImp.get(), coinType));
            }
        }
        else if (type == BaseWallet::WalletTypePrivateKey)
        {
            std::string private_key = loadPrivateKey(wallet_key);
            if (private_key.empty())
                return nullptr;
            auto tw_data = TW_DATA(private_key.c_str());
            privateKey = TW_WRAP(TWPrivateKey, TWPrivateKeyCreateWithData(tw_data.get()));
        }

        return privateKey;
    }

    // 创建钱包
    std::string register_wallet(int strength, const std::string& password)
    {
        auto walletImpl = TW_WRAP(TWHDWallet, TWHDWalletCreate(strength, TW_STRING(password.c_str()).get()));
        std::string mnemonic = TWStringUTF8Bytes(TWHDWalletMnemonic(walletImpl.get()));
        return BaseWallet::save_wallet(mnemonic, password);
    }

    // 验证助记词
    bool isValidMnemonic(const std::string& strMnemonic)
    {
        bool isValid = true;

        auto secretMnemonic = TWStringCreateWithUTF8Bytes(strMnemonic.c_str());
        if (!TWHDWalletMnemonicTranslate(secretMnemonic, TWMnemonicLanguageEnglish))
        {
            isValid = false;
        }

        if (!TWHDWalletIsValid(secretMnemonic))
        {
            isValid = false;
        }
        TWStringDelete(secretMnemonic);

        return isValid;
    }

    // 导入助记词
    std::string importMnemonic(const std::string& strMnemonic, const std::string& password)
    {
        if (!isValidMnemonic(strMnemonic))
            return "";

        auto secretMnemonic = TWStringCreateWithUTF8Bytes(strMnemonic.c_str());
        TWHDWalletMnemonicTranslate(secretMnemonic, TWMnemonicLanguageEnglish);
        std::string enMnemonic = TWStringUTF8Bytes(secretMnemonic);
        TWStringDelete(secretMnemonic);

        auto walletImp = TrustWallet::getWallet(enMnemonic, password);
        std::string mnemonic = TWStringUTF8Bytes(TWHDWalletMnemonic(walletImp.get()));
        return BaseWallet::save_wallet(mnemonic, password);
    }

    // 验证助记词
    bool isValidPrivateKey(TWCoinType coinType, const std::string& privateKey)
    {
        bool isValid = true;

        auto tw_data = TW_DATA(privateKey.c_str());
        auto tw_privateKey = TWPrivateKeyCreateWithData(tw_data.get());

        if (tw_privateKey == nullptr)
        {
            isValid = false;
        }

        TWPrivateKeyDelete(tw_privateKey);

        return isValid;
    }

    // 导入私钥
    std::string importPrivateKey(TWCoinType coinType, const std::string& privateKey)
    {
        if (!isValidPrivateKey(coinType, privateKey))
            return "";

        return BaseWallet::save_wallet(privateKey);
    }

    TWCurve getCurve(const std::string& curve)
    {
        if (curve.compare("secp256k1") == 0)
        {
            return TWCurveSECP256k1;
        }
        else if (curve.compare("ed25519") == 0)
        {
            return TWCurveED25519;
        }
        else if (curve.compare("ed25519-blake2b-nano") == 0)
        {
            return TWCurveED25519Blake2bNano;
        }
        else if (curve.compare("curve25519") == 0)
        {
            return TWCurveCurve25519;
        }
        else if (curve.compare("nist256p1") == 0)
        {
            return TWCurveNIST256p1;
        }
//        else if (curve.compare("ed25519-cardano-seed") == 0)
//        {
//            return TWCurveED25519Extended;
//        }
        return TWCurveNone;
    }

}


#endif //JETPACK_TEST_WALLET_H
