# DocumentHook

- 通过Hook，当有APP申请访问【Android/data】目录时，自动调用代码授权，省去**两个步骤**

  >1. 需要点击【使用此文件夹】
  >2. 需要点击弹窗按钮【允许】
  >
  >本软件就是省略了这两个步骤，效果和设置自动点击器差不多，理论上本软件更快

- 仅适用于Android13，事实上低版本也没有用处，所以低版本无法安装

- 无主界面，LSPosed作用域给【文件】app 即可，如有其他包名【文件】可以联系我加，或手动编译/反编译

  > ```xml
  > com.android.documentsui
  > com.google.android.documentsui
  > ```

- 配合批量授权效果更佳，如【MT管理器】、【存储空间清理】、【雪豹速清】等App

- 软件意义：适用于未申请root权限，但需要大量申请【Android/data】目录授权

- 软件分为**两个版本**

  - **Auth-one**

    > 此版本是申请一个授权一个，取决于目标应用（如【MT管理器】）的申请，作用域**仅给【文件】app即可**

  - **Auth-all**

    > 此版本通过Hook目标应用（如【MT管理器】app），可以一次性授权所有应用（存在个别的应用无法授权）
    >
    > 已经在【MT管理器】app、【雪豹速清】app 测试通过
    >
    > 此版本LSPosed作用域为**目标应用**（如【MT管理器】app），和**【文件】app**

# DocumentHook

- **machine translation**

- Through Hook, when there is an APP application to access the [Android/data] directory, the code authorization will be automatically invoked, saving **two steps**

  >1. You need to click [Use this folder]
  >2. You need to click the confirm button [Allow]
  >
  > This software just omits these two steps, the effect is similar to setting an automatic clicker, theoretically this software is faster

- Only applicable to Android13, in fact, the lower version is useless, so the lower version cannot be installed

- No main interface, LSPosed scope can be given to [file] app, if you have other package name [file], you can contact me to add, or manually compile/decompile

  > ```xml
   > com.android.documentsui
   > com.google.android.documentsui
   > ```

- The effect of batch authorization is better, such as [MT Manager], [存储空间清理], [雪豹速清] and other apps

- The meaning of the app: Applicable to users who have not applied for root permissions, but need to apply for [Android/data] directory authorization in large quantities

- The software is divided into **two versions**

  - **Auth-one**

    > This version is to apply for one authorization, depending on the application of the target application (such as [MT Manager]), the scope is **only for the [File] app**

  - **Auth-all**

    > This version can authorize all applications at one time through the Hook target application (such as [MT Manager] app) (some applications cannot be authorized)
    >
    > It has passed the test in [MT Manager] app and [雪豹速清] app
    >
    > The **scope** of this version of LSPosed is the **target application** (such as [MT Manager] app), and **[File] app**

