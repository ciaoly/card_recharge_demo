package com.example.card_recharge_demo.utils

import android.nfc.tech.MifareClassic
import androidx.compose.ui.text.toUpperCase

class KeyUtils private constructor() {
    val keyMap: MutableMap<String, ByteArray> = mutableMapOf()

    fun getKeyA(uid: ByteArray): ByteArray {
        val _uid = BytesUtils.bytes2Hex(uid) // .substring(0 until 8).uppercase()
        if(keyMap.containsKey(_uid)) {
            return keyMap.getValue(_uid)
        }
        return MifareClassic.KEY_DEFAULT
    }

    fun getKeyB(uid: ByteArray): ByteArray {
        return getKeyA(uid)
    }

    fun getKeyR(uid: ByteArray): ByteArray {
        return getKeyA(uid);
    }

    fun getKeyW(uid: ByteArray): ByteArray {
        return getKeyB(uid);
    }

    companion object {
        val instance: KeyUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            KeyUtils()
        }
    }
}