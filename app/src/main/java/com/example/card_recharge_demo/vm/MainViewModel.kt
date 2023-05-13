package com.example.card_recharge_demo.vm

import android.nfc.Tag
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.card_recharge_demo.model.CardData
import com.example.card_recharge_demo.model.UnsupportedCardException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {
    lateinit private var _cardData: CardData
    val balance = MutableLiveData("0.0")
    val loading = MutableLiveData(false)
    private val _statusCode = MutableSharedFlow<StatusCode>()
    val statusCode = _statusCode.asSharedFlow()

    fun onCardAttached(m1Tag: Tag) {
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _cardData = CardData(m1Tag)
                balance.postValue(_cardData.balanceStr)
                _statusCode.emit(StatusCode.READ_SUCCESS)
            } catch (e: UnsupportedCardException) {
                _statusCode.emit(StatusCode.READ_FAILED)
            } catch (e: Exception) {
                _statusCode.emit(StatusCode.READ_EXCEPTION)
            } finally {
                loading.postValue(false)
            }
        }
    }

//    fun topUp(yuan: Double) {
//        loading.postValue(true)
//        if(!this::_cardData.isInitialized ) {
//            operationSuccess.postValue(false)
//            loading.postValue(false)
//            return
//        }
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = _cardData.topUp(yuan)
//            operationSuccess.postValue(result)
//                loading.postValue(false)
//        }
//    }

    fun topUp(fen: Int) {
        val def = CompletableDeferred<StatusCode>()
        loading.postValue(true)
        viewModelScope.launch {
            val status = def.await()
            _statusCode.emit(status)
        }
        if(!this::_cardData.isInitialized ) {
            loading.postValue(false)
            def.complete(StatusCode.NO_CARD)
        }
        viewModelScope.launch(Dispatchers.Main) {
            delay(5000)
            if(!def.isCompleted) {
                def.complete(StatusCode.TIMEOUT)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = _cardData.topUp(fen)
                if(result) {
                    balance.postValue(_cardData.balanceStr)
                    def.complete(StatusCode.WRITE_SUCCESS)
                } else {
                    def.complete(StatusCode.WRITE_FAILED)
                }
            } catch (e: Exception) {
                def.complete(StatusCode.WRITE_EXCEPTION)
            } finally {
                loading.postValue(false)
            }
        }
    }

//    fun topUp(fen: Int): CompletableDeferred<StatusCode> {
//        val def = CompletableDeferred<StatusCode>()
//        loading.postValue(true)
//        if(!this::_cardData.isInitialized ) {
//            loading.postValue(false)
//            def.complete(StatusCode.NO_CARD)
//        }
//        viewModelScope.launch(Dispatchers.Main) {
//            delay(5000)
//            if(!def.isCompleted) {
//                def.complete(StatusCode.TIMEOUT)
//            }
//        }
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val result = _cardData.topUp(fen)
//                if(result) {
//                    balance.postValue(_cardData.balanceStr)
//                    def.complete(StatusCode.WRITE_SUCCESS)
//                } else {
//                    def.complete(StatusCode.WRITE_FAILED)
//                }
//            } catch (e: Exception) {
//                def.complete(StatusCode.WRITE_EXCEPTION)
//            } finally {
//                loading.postValue(false)
//            }
//        }
//        return def
//    }

}

enum class StatusCode{
    NO_CARD, READ_SUCCESS, READ_FAILED, READ_EXCEPTION, WRITE_SUCCESS, WRITE_FAILED, WRITE_EXCEPTION, TIMEOUT
}
