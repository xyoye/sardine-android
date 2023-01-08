package com.xyoye.sardine

import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.xyoye.sardine.impl.OkHttpSardine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val sardine = OkHttpSardine(UnsafeOkHttpClient.client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvResult = findViewById<TextView>(R.id.tv_result)
        tvResult.movementMethod = ScrollingMovementMethod.getInstance()

        val etUrl = findViewById<EditText>(R.id.et_url)
        val etAccount = findViewById<EditText>(R.id.et_account)
        val etPwd = findViewById<EditText>(R.id.et_pwd)

        findViewById<Button>(R.id.bt_connect).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val result = testConnect(
                    etUrl.text?.toString(),
                    etAccount.text?.toString(),
                    etPwd.text?.toString()
                )
                withContext(Dispatchers.Main) {
                    tvResult.text = result
                }
            }
        }
    }

    private suspend fun testConnect(
        url: String?,
        account: String?,
        pwd: String?
    ): String {
        if (TextUtils.isEmpty(url)) {
            tips("url不能为空")
            return ""
        }
        if (TextUtils.isEmpty(account).not() && TextUtils.isEmpty(pwd).not()) {
            sardine.setCredentials(account, pwd)
        }

        try {
            return sardine.list(url).joinToString(separator = "\n") { it.name }
        } catch (e: Exception) {
            e.printStackTrace()
            tips(e.message ?: "打开目录失败")
        }
        return ""
    }

    private suspend fun tips(msg: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }

}