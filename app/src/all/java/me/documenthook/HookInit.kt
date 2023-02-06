package me.documenthook

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * @author littleWhiteDuck
 */
class HookInit : IXposedHookLoadPackage {
    private var appUris: Array<Uri>? = null
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val hostPackageName = lpparam.packageName
        if (hostPackageName == "com.android.documentsui" || hostPackageName == "com.google.android.documentsui") {
            XposedHelpers.findAndHookMethod(Application::class.java,
                "attach",
                Context::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (appUris != null) return
                        appUris = getAppUris(param.args[0] as Context)
                    }
                })

            XposedHelpers.findAndHookMethod("com.android.documentsui.picker.PickFragment",
                lpparam.classLoader,
                "updateView",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val obj = param.thisObject
                        val mInjector = XposedHelpers.getObjectField(obj, "mInjector") ?: return
                        val actions = XposedHelpers.getObjectField(mInjector, "actions") ?: return
                        XposedHelpers.callMethod(actions, "finishPicking", appUris)
                    }
                })
        } else {
            XposedHelpers.findAndHookMethod(
                Activity::class.java,
                "onCreate",
                Bundle::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val activity = param.thisObject as Activity
                        val uri =
                            "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2F$hostPackageName/document/primary%3AAndroid%2Fdata%2F$hostPackageName"
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                        intent.flags =
                            (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(uri))
                        activity.startActivityForResult(intent, 520)
                    }
                })

            XposedHelpers.findAndHookMethod(Activity::class.java,
                "onActivityResult",
                Int::class.java,
                Int::class.java,
                Intent::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val requestCode = param.args[0] as Int
                        val resultCode = param.args[1] as Int
                        val intent = param.args[2] as Intent
                        val activity = param.thisObject as Activity
                        if (requestCode == 520 && resultCode == Activity.RESULT_OK) {
                            val clipData = intent.clipData ?: return
                            var count = 0
                            for (i in 0 until clipData.itemCount) {
                                val uri = clipData.getItemAt(i).uri
                                val takeFlags: Int =
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                activity.contentResolver.takePersistableUriPermission(
                                    uri, takeFlags
                                )
                                count++
                            }
                            showFinishDialog(
                                activity, """
                                完成！，共授权 $count 项，请停止激活模块！！！可能存在个别应用未被授权，手动授权即可。
                                Completed! A total of $count items are authorized, please stop activating the module!!! There may be some applications that are not authorized, just manually authorize.
                            """.trimIndent()
                            )
                        }
                        param.result = null
                    }
                })
        }
    }


    /**
     * 未考虑是否已经授权、/Android/data/目录下是否有相关应用文件夹
     */

    @SuppressLint("QueryPermissionsNeeded")
    private fun getAppUris(context: Context): Array<Uri> {
        val packageInfoList = context.packageManager.getInstalledPackages(
            PackageManager.PackageInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
        val uris = ArrayList<Uri>()
        packageInfoList.forEach {
            uris.add(Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2F${it.packageName}"))
        }
        return uris.toTypedArray()
    }

    private fun showFinishDialog(activity: Activity, message: String) {
        AlertDialog.Builder(activity)
            .setTitle("Tip")
            .setMessage(message)
            .setPositiveButton("ok", null)
            .create()
            .show()
    }
}