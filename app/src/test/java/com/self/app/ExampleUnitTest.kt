package com.self.app

import kotlinx.coroutines.CoroutineScope
import org.junit.Test

import org.junit.Assert.*
import kotlin.coroutines.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    lateinit var myCoroutine:Continuation<Unit>

    val name:String = String()

    suspend fun yield(){
        suspendCoroutine<Unit> {
            myCoroutine = it
        }
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

         val _myCoroutine = suspend {
             println("这是我定义的携程--1")
             yield()
             println("这是我定义的携程--2")
        }.createCoroutine(
            object : Continuation<Unit> {
                override val context: CoroutineContext
                    get() = EmptyCoroutineContext

                override fun resumeWith(result: Result<Unit>) {
                    println("resumeWidth")
                }
            }
        )
        println("开始允许")
        _myCoroutine.resume(Unit)
        println("结束运行")

    }



}