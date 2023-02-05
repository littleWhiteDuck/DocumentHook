package me.documenthook

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
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
            XposedHelpers.findAndHookMethod(
                Application::class.java,
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
                        val mPickTarget = XposedHelpers.getObjectField(obj, "mPickTarget") ?: return
                        val mInjector = XposedHelpers.getObjectField(obj, "mInjector") ?: return
                        val actions = XposedHelpers.getObjectField(mInjector, "actions") ?: return
                        val uri = XposedHelpers.callMethod(mPickTarget, "getTreeDocumentUri") as Uri
                        XposedHelpers.callMethod(actions, "finishPicking", appUris)
                    }
                })
        }
    }


    private fun getAppUris(context: Context): Array<Uri> {
        val packageInfoList = context.packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
        val uris = ArrayList<Uri>()
        packageInfoList.forEach {
            uris.add(Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2F${it.packageName}"))
        }
        return uris.toTypedArray()
    }
}