package com.self.app;

public class StringFormat {


    public static String format(String str,int startRetain,int endRetain,int centerCharCount){
        if (str.length()<=(startRetain+endRetain)){
            return str;
        }
        int len = str.length();
        String tmp = str.substring(0,startRetain);
        for (int i=0;i<centerCharCount;i++){
            tmp+='*';
        }
        tmp+=str.substring(len-endRetain,len);
        return tmp;
    }

}
