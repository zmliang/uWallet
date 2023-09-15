package com.zml.wallet;


import android.content.Context;

import java.util.Map;

class NativeLib {

    static {
        System.loadLibrary("wallet");
    }

    /**
     * A native method that is implemented by the 'wallet' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void _init();

    public void init(Context context){
        _init();
        WalletDb.init(context);
    }

    /**
     * 创建钱包
     * @param pwd
     * @return
     */
    public native String createWallet(String pwd);

    /**
     * 导入住记词
     * @param mnemonic
     * @param pwd
     * @return wallet key
     */
    public native String importMnemonic(String mnemonic,String pwd);

    /**
     * 导入私钥
     * @param privateKey
     * @param coinType
     * @return
     */
    public native String importPrivateKey(String privateKey,int coinType);


    /**
     * 获取 私钥
     * @param walletKey
     * @param pwd
     * @param coinType
     * @return
     */
    public native String getPublicChainPrivateKey(String walletKey,String pwd,int coinType);

    /**
     * 获取 公钥
     * @param walletKey
     * @param pwd
     * @param coinType
     * @return
     */
    public native Map<String,String> getPublicChainPublicKey(String walletKey, String pwd, int coinType);

    /**
     * 获取 公链地址
     * @param walletKey
     * @param pwd
     * @param coinType
     * @return
     */
    public native String getPublicChainAddress(String walletKey,String pwd,int coinType);


    public native String getMnemonic(String walletKey,String pwd);

}