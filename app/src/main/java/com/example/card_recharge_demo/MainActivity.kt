package com.example.card_recharge_demo

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.card_recharge_demo.ui.page.MainPage
import com.example.card_recharge_demo.ui.theme.Card_recharge_demoTheme
import com.example.card_recharge_demo.utils.BytesUtils
import com.example.card_recharge_demo.utils.KeyUtils
import com.example.card_recharge_demo.utils.M1CardUtils
import com.example.card_recharge_demo.vm.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties


class MainActivity : ComponentActivity() {

    private val mainVM by viewModels<MainViewModel>()
    private var nfcAdapter: NfcAdapter? = null
    private val flag = (NfcAdapter.FLAG_READER_NFC_A)
    private val keysInitFlag = MutableLiveData<Boolean>(false)

    private fun checkNFC() {
        if( nfcAdapter == null ) {
            Toast.makeText(this@MainActivity, "设备不支持NFC, 无法使用该软件", Toast.LENGTH_LONG).show()
            finish()
        } else if (!nfcAdapter!!.isEnabled) {
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            Toast.makeText(this@MainActivity, "请开启NFC开关, 否则无法使用该软件", Toast.LENGTH_LONG).show()
        }
        if(nfcAdapter!!.isEnabled) {
            nfcAdapter!!.enableReaderMode(this, NfcAdapter.ReaderCallback {
                val _tag = it
                if(keysInitFlag.value != true) {
                    keysInitFlag.observe(this, Observer {
                        if(it == true) {
                            mainVM.onCardAttached(_tag)
                        }
                    })
                } else {
                    mainVM.onCardAttached(_tag)
                }
            }, flag, null)
        }
    }

    private fun getTagByIntent(intent: Intent) {
        val _tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        if (_tag != null) {
            if(keysInitFlag.value != true) {
                keysInitFlag.observe(this, Observer {
                    if(it == true) {
                        mainVM.onCardAttached(_tag)
                    }
                })
            } else {
                mainVM.onCardAttached(_tag)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            val properties = Properties()
            properties.load(applicationContext.assets.open("key.prop"))
            for((uid, key) in properties) {
                KeyUtils.instance.keyMap.put(uid.toString(), BytesUtils.hex2Bytes(key.toString()))
            }
            withContext(Dispatchers.Main) {
                keysInitFlag.value = true;
            }
        }
        getTagByIntent(intent)
        nfcAdapter = M1CardUtils.isNfcAble(this)
        setContent {
            Card_recharge_demoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage(vm = mainVM)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        getTagByIntent(intent)
    }

    override fun onPause() {
        super.onPause()
        if(nfcAdapter!!.isEnabled) {
            nfcAdapter!!.disableReaderMode(this)
        }
    }

    override fun onResume() {
        super.onResume()
        checkNFC()
    }
}

val defaultElevation = 6.dp