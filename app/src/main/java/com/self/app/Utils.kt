package com.self.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.widget.EditText
import com.zml.wallet.TokenInfo
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

fun getPixels(dipValue: Int, context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dipValue.toFloat(),
        r.displayMetrics
    ).toInt()
}

var TokenInfo.isHeader:Boolean
    get()=this.symbol.equals("HEADER")
    set(value) {this.symbol = "HEADER"}


/**
 * 根据手机的分辨率sp 转成px(像素)
 */
inline val Double.sp: Int get() = run {
    toFloat().sp
}

inline val Int.sp: Int get() = run {
    toFloat().sp
}

inline val Float.sp: Int get() = run {
    val scale: Float = Resources.getSystem().displayMetrics.scaledDensity
    return (this * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率dp 转成px(像素)
 */
//使用示例 10.dp
inline val Double.dp: Int get() = run {
    return toFloat().dp
}

inline val Int.dp: Int get() = run {
    return toFloat().dp
}

inline val Float.dp: Int get() = run {
    val scale: Float = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

inline val Float.toDp:Int get() = run{
    val scale: Float = Resources.getSystem().displayMetrics.density
    return (this/scale+0.5f).toInt()
}

inline val Int.toDp:Int get() = run{
    return toFloat().toDp
}

fun EditText.setSizeHint(hint:String,size:Int){
    //设置hint字体，及大小
    val ss = SpannableString(hint) //定义hint的值
    val ass = AbsoluteSizeSpan(size, true) //设置字体大小 true表示单位是sp
    ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.hint = SpannedString(ss)
}

/**
 * 字符串处理
 */
fun String.encipherFormat(startRetain:Int = 4,
                    endRetain:Int = 8,
                    centerChar: Char = '*',
                    centerCharCount:Int = 4):String{
    if (this.length<=startRetain+endRetain){
        return this
    }
    val len= this.length

    var tmp = this.substring(0,startRetain)
    for (i in 0..centerCharCount){
         tmp+=(centerChar)
    }
    tmp+=(this.substring(len-endRetain,len))
    return tmp
}

fun String.isNumber():Boolean{
    return if (this.isNullOrEmpty()) false else this.all { Character.isDigit(it) }
}



@SuppressLint("SimpleDateFormat")
fun String.formatDate():String{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(this.toLong()*1000)) // 时间戳转
}





