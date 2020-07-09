package com.smartahc.android.aiot.splitdemo

import android.Manifest
import android.os.Bundle
import com.smartahc.android.simple.BaseActivity
import com.smartahc.android.splitcore_support_v4.PageParams
import com.smartahc.android.splitcore_support_v4.SmartUser
import com.smartahc.android.splitcore_support_v4.SplitCore
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
        SplitCore.onSplitCreate(this, SmartUser("你的账户", "你的密码"))
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
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    
}
