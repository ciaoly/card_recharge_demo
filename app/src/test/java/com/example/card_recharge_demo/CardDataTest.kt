package com.example.card_recharge_demo

import com.example.card_recharge_demo.dao.CardData
import com.example.card_recharge_demo.utils.BytesUtils
import org.junit.Test

class CardDataTest {
    val cardData = CardData(0u)

    @Test
    fun testTopUp() {
        cardData.topUp(3528)
//        balance.topUp(550)
        println(cardData.toString())
        testToBytes()
        testCheckCode()
//        cardData.topUp(0xffff0000)
        println(cardData.toString())
        testToBytes()
        testCheckCode()
        cardData.topUp(0x0dda)
        println(cardData.toString())
        testToBytes()
        testCheckCode()
    }

    fun testToBytes() {
        println(BytesUtils.bytes2Hex(cardData.toBytes()));
    }

    fun testCheckCode() {
        println(BytesUtils.bytes2Hex(cardData.checkCode));
    }
}