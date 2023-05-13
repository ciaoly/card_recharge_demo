package com.example.card_recharge_demo.model

import com.example.card_recharge_demo.dao.CardData
import com.example.card_recharge_demo.utils.M1CardUtils
import android.nfc.Tag
import com.example.card_recharge_demo.utils.BytesUtils
import com.example.card_recharge_demo.utils.KeyUtils

class CardData(val m1Tag: Tag) {
    private var _cardData: CardData
    private val _balanceBlock = 30

    init {
        val key = KeyUtils.instance.getKeyR(m1Tag.id)
        val _readBytes = M1CardUtils.readBlock(m1Tag, _balanceBlock, key)
        if(_readBytes.size < 4) {
            throw UnsupportedCardException(BytesUtils.bytes2Hex(m1Tag.id), key)
        }
        val _b = BytesUtils.bytes2IntLittle(_readBytes.sliceArray(0 until 4)).toUInt()
        _cardData = CardData(_b)
    }

    val balance get() = _cardData.balance
    val balanceStr get() = "${_cardData.yuan}.${_cardData.fen}"

    fun refreshBalance() {
        val key = KeyUtils.instance.getKeyR(m1Tag.id)
        val _readBytes = M1CardUtils.readBlock(m1Tag, _balanceBlock, key)
        if(_readBytes.size < 4) {
            throw UnsupportedCardException(BytesUtils.bytes2Hex(m1Tag.id), key)
        }
        _cardData.balance = BytesUtils.bytes2IntLittle(_readBytes.sliceArray(0 until 4)).toUInt()
    }

    fun recharge(): Boolean {
//        val _balanceBytes = byteArrayOf(*(_cardData.toBytes()))
        var result =  M1CardUtils.writeBlock(m1Tag, _balanceBlock, KeyUtils.instance.getKeyW(m1Tag.id),
            _cardData.toBytes())
        if(result) {
            result = M1CardUtils.writeBlock(m1Tag, _balanceBlock - 1, KeyUtils.instance.getKeyW(m1Tag.id),
                _cardData.toBytes())
        }
        return if(result) {
            val _wish = _cardData.balance
            refreshBalance()
            _cardData.balance == _wish
        } else {
            false;
        }
    }

    fun topUp(yuan: Double): Boolean {
        _cardData.topUp(yuan)
        return recharge();
    }

    fun topUp(fen: Int): Boolean {
        _cardData.topUp(fen)
        return recharge();
    }

    override fun toString(): String {
        return _cardData.toString()
    }
}

class UnsupportedCardException(uid: String = "", key: ByteArray = byteArrayOf() ) : Throwable() {
    override val message: String?
        get() = "密码错误或该卡片不受支持"
}
