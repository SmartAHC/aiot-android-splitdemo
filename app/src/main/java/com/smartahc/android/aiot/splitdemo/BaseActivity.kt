package com.smartahc.android.simple

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import java.util.concurrent.atomic.AtomicBoolean


/**
 * 权限申请
 */
abstract class BaseActivity : Activity() {

    companion object{
        const val REQUEST_PERMISSIONS = 10001
    }

    /**
     * 是否有权限
     */
    protected val hasPermission: AtomicBoolean = AtomicBoolean(false)


    /**
     * 检测权限
     */
    protected fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = this.getPermissions()
            if (permissions.isEmpty()) {
                return
            }
            var grantedCount = 0

            for (permission in permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    break
                }
                grantedCount++
            }
            if (grantedCount == permissions.size) {
                // 全部通过
                hasPermission.set(true)
            } else {
                hasPermission.set(false)
                requestPermissions(permissions, REQUEST_PERMISSIONS)
            }
        }
    }

    /**
     * 权限判断
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            var permissionCount = 0
            // 直到有权限为止
            for ((index, value) in grantResults.withIndex()) {
                if (value != PackageManager.PERMISSION_GRANTED) {
                    val isTips = ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permissions[index]
                    )
                    if (isTips) {
                        checkPermission()
                    } else {
                        intoPermissionSettingPage()
                    }
                    break
                }
                permissionCount += 1
            }
            if (permissionCount == permissions.size) {
                hasPermission.set(true)
            }
        }
    }


    /**
     * 进入权限页面
     */
    protected fun intoPermissionSettingPage(){
        this.showToastMsg("无权限，请全部授权")
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        intent.data = Uri.fromParts("package", packageName, null)
        this.startActivity(intent)
        finish()
    }


    /**
     * 获取权限列表
     */
    protected abstract fun getPermissions(): Array<String>

    /**
     * 显示信息
     */
    protected fun showToastMsg(msg: String) {
        this.runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

}