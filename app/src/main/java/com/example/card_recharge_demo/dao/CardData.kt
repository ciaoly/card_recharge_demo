package com.example.card_recharge_demo.dao

import com.example.card_recharge_demo.utils.BytesUtils
import kotlin.math.roundToInt

class CardData(balance: UInt) {
    var balance: UInt

    init {
        this.balance = balance
    }
    constructor(yuan: Double) : this((yuan * 100).roundToInt().toUInt())

    val fen
        get() = balance % 100u
//        set(v) {
//            balance = balance / 100u * 100u + v
//        }

    val yuan
        get() = balance / 100u
//        set(v) {
//            balance = balance % 100u + v * 100u
//        }

    fun topUp(yuan: Double) {
        balance += (yuan * 100).roundToInt().toUInt()
    }

    fun topUp(fen: Int) {
        if(fen >= 0) {
            balance += fen.toUInt()
        } else {
            balance -= Math.abs(fen).toUInt()
        }

    }

    fun toBytes(): ByteArray {
        val _balanceBytes = BytesUtils.int2BytesLittle(balance.toInt())
        return byteArrayOf(*_balanceBytes, *checkCode, *_balanceBytes, 0x1e, 0xe1.toByte(), 0x1e,
            0xe1.toByte())
    }

    val checkCode get() = BytesUtils.int2BytesLittle((0xffffffffu - balance).toInt())

    override fun toString(): String {
        return BytesUtils.bytes2Hex(toBytes())
    }
}