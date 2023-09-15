package com.zml.wallet.web3;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class DecimalFormat {

    public static String div(BigInteger balance,int tokenDecimal,int scale){
        StringBuilder sb = new StringBuilder("1");
        while (tokenDecimal>0){
            sb.append("0");
            tokenDecimal--;
        }
        BigDecimal decimal = new BigDecimal(new BigInteger(sb.toString()));
        return new BigDecimal(balance).divide(decimal, scale, RoundingMode.HALF_DOWN).toString();
    }

}
