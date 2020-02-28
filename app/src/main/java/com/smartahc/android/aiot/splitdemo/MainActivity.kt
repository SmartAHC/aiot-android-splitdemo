package com.smartahc.android.aiot.splitdemo

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import com.smartahc.android.simple.BaseActivity
import com.smartahc.android.splitcore.Common
import com.smartahc.android.splitcore.PageParams
import com.smartahc.android.splitcore.SmartUser
import com.smartahc.android.splitcore.SplitCore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    /**
     * 申请权限
     */
    override fun getPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 申请权限
        checkPermission()
        // Step 1：注意：请联系商务获取账户密码！！
        SplitCore.onSplitCreate(this, SmartUser("15626093012", "1234567"))
        // 测试初始化
        btnInit.setOnClickListener {
            SplitCore.onSplitResume()
        }
        // 功能：进入菜单
        btnMenu.setOnClickListener {
            SplitCore.onSkipMenu()
        }
        // 功能：进入戴标界面
        btnWearTag.setOnClickListener {
            // 注意：请登录网址端获取配置！！
            val params =
                "{\"id\":1,\"func\":1,\"hardware_selection\":[\"耳标钳\",\"伸缩阅读器\"],\"categoryfunc\":147}"
            SplitCore.onSkipPage(params)
        }
        // 功能：进入戴标界面
        btnWearTagParams.setOnClickListener {
            // 注意：请登录网址端获取配置！！
            SplitCore.onSkipPage(
                PageParams(
                    1, 1, 1, listOf(
                        "耳标钳", "伸缩阅读器"
                    )
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // Step 2
        SplitCore.onSplitResume()
        registryBroadcast()
    }

    override fun onDestroy() {
        unRegistryBroadcast()
        super.onDestroy()
    }

    /**
     * 注册广播
     */
    private fun registryBroadcast() {
        registerReceiver(broadcast, IntentFilter(Common.SERVICE_BROADCAST_ACTION_FILTER))
    }

    /**
     * 解注册
     */
    private fun unRegistryBroadcast() {
        unregisterReceiver(broadcast)
    }

    /**
     * 接收
     */
    private val broadcast = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Common.SERVICE_BROADCAST_ACTION_FILTER -> {
                    // 获取数据
                    val earTag = intent.getStringExtra(Common.SERVICE_BROADCAST_KEY_EARTAG)
                    runOnUiThread {
                        tvEartag.text = earTag
                    }
                }
            }
        }
    }

}
