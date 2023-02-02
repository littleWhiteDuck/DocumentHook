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

- 配合批量授权效果更佳，如MT管理器、存储空间清理等

- 软件意义：适用于未申请root权限，但需要大量申请【Android/data】目录授权