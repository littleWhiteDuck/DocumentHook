package me.documenthook

import android.net.Uri
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * @author littleWhiteDuck
 */
class HookInit : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val hostPackageName = lpparam.packageName
        if (hostPackageName == "com.android.documentsui" || hostPackageName == "com.google.android.documentsui") {
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
                        XposedHelpers.callMethod(actions, "finishPicking", arrayOf(uri))
                    }
                })
        }
    }
}