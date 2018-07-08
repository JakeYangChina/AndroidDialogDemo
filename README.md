# android-dialog-library
对话框库，使用注解声明，任意类内都可调用，无论子线程还是UI线程均可调用，使用起来很方便
<br>说明：此dialog框架可以在任意类内调用，```例如：Activity，Fragment，Service 或者是其它类内都可以使用```
## 使用说明
### @CoreDialogShow注解
当对话显示时，就会执行此方法（可以声明多个，但方法名不可以相同，请求码requestCode值不能相同，请求码requestCode值相同代表同一组请求）<br>注解内可以指定requestCode值，不指定即为默认值<br>例如：<br>
```
    @CoreDialogShow(requestCode = 2)//requestCode可以不指定，使用默认值
    public void dialogShow() {
        Log.e(TAG, "dialogShow");
    }
```
### @CoreDialogClickOk注解
注解内可以指定requestCode值，不指定即为默认值
当点击确认按钮时，就会执行此方法，当使用自己定义的布局文件时，需要指定```确定按钮```的id值
可以不指定确定按钮的id
<br>例如：<br>
```
    @CoreDialogClickOk(requestCode = 2, id = R.id.dialogClickOk)//当使用自己定义的布局文件时，需要指定id值
    public void dialogClickOk() {
        Log.e(TAG, "dialogClickOk");
    }
```
### @CoreDialogClickNo注解
注解内可以指定requestCode值，不指定即为默认值
当点击取消按钮时，就会执行此方法，当使用自己定义的布局文件时，需要指定```取消按钮```的id值
<br>例如：<br>
```
    @CoreDialogClickNo(requestCode = 2, id = R.id.dialogClickNo)//当使用自己定义的布局文件时，需要指定id值
    public void dialogClickNo() {
        Log.e(TAG, "dialogClickNo");
    }
```
### @CoreDialogCancel注解
注解内可以指定requestCode值，不指定即为默认值
当对话框被取消了，就会执行此方法
<br>例如：<br>
```
    @CoreDialogCancel(requestCode = 2)
    public void dialogCancel() {
        Log.e(TAG, "dialogCancel");
    }
```
### @CoreDialogClose注解
注解内可以指定requestCode值，不指定即为默认值
当对话框被关闭了，就会执行此方法
<br>例如：<br>
```
    @CoreDialogClose(requestCode = 2)
    public void dialogClose() {
        Log.e(TAG, "dialogClose");
    }
```
### 上面五个注解为一组请求，requestCode控制是否是同一组请求，可以定义多组请求，通过requestCode区分显示哪组对话框
### 通过如下方法显示指定对话框
```
    //方法一：必须先初始化 init() 方法，调用两个参数的方法，参数一：当前类（使用注解的类对象），参数二：指定显示哪组对话框（requestCode = 2）
    CoreDialog.builder()
                .setCanceledOnTouchOutside(false)
                .setContentView(View.inflate(this, R.layout.default_dialog, null))
                .build()
                .showDialog(this, 2);
    
    //方法二：必须先初始化 init() 方法，调用两个参数的方法，参数一：当前类（使用注解的类对象），显示requestCode为默认值的对话框
    CoreDialog.builder()
                .setCanceledOnTouchOutside(false)
                .setContentView(View.inflate(this, R.layout.default_dialog, null))
                .build()
                .showDialog(this);
```

<br>
<hr>
<br>

## API说明：CoreDialog类，主要是用于显示对话框
#### ```public static void init(Application application)```
作用：初始化Context<br>此方法放到Application内初始化（必须初始化，才能使用）
#### ```public void showDialog(Object curObj)```
作用：显示默认请求码的对话框<br>参数一：当前类对象（必须是被注解修饰的类对象）
#### ```public void showDialog(Object curObj, int requestCode)```
作用：显示指定请求码的对话框<br>参数一：当前类对象，参数二：请求码（显示指请求码的对话框）<br>
#### ```public static void closeDialog(Object curObj)```
作用：关闭默认请求码的对话框<br>参数为：当前类对象
#### ```public static void closeDialog(Object curObj, int requestCode)```
作用：关闭指定请求码的对话框<br>参数为一：当前类对象，参数二：请求码（指定要关闭的对画框）
#### ```public static void destroyDialog(Object curObj)```
作用：释放默认请求码的对话框，防止内存泄漏<br>建议在关闭当前页面时释放内存
#### ```public static void destroyDialogAll()```
作用：释放所有内存，防止内存泄漏<br>建议在程序退出时调用，或最后一个页面关闭时调用

<br>
<hr>
<br>

## API说明：CoreDialog类，主要是用于显示对话框
#### ```public CoreDialogBuilder setTitle(String title)```
作用：设置标题<br>当没有指定自定义的布局文件时才会生效
#### ```public CoreDialogBuilder setMessage(String message)```
作用：设置消息<br>当没有指定自定义的布局文件时才会生效
#### ```public CoreDialogBuilder setOkButtonName(String okButtonName)```
作用：设置确认按钮的文字<br>当没有指定自定义的布局文件时才会生效
#### ```public CoreDialogBuilder setNoButtonName(String noButtonName)```
作用：设置取消按钮的文字<br>当没有指定自定义的布局文件时才会生效
#### ```public CoreDialogBuilder setContentView(View view)```
作用：设置自定义的布局文件<br>需要在注解内指定确认按钮和取消按钮的id
#### ```public CoreDialogBuilder setCanceledOnTouchOutside(boolean canceledOnTouchOutside)```
作用：设置是否触碰到对话框以为的地方，取消显示对话框，默认是true<br>
#### ```public CoreDialogBuilder setCancelable(boolean cancelable)```
作用：设置是否点击返回键取消对话框显示，默认是true<br>
#### ```public CoreDialogBuilder setIsCanModifyLayout(boolean isCanModifyLayout)```
作用：设置是否可以在布局文件内控制布局文件的大小，默认是false<br>例如：设置宽高相同的对画框

<br>
<hr>
<br>

## 从github clone 代码到本地放到AS后，发现并不能点“Run”键运行app，当强制点击运行app，会弹出窗口，在最下方提示Error:Please select Android SDK。解决办法如下：
解决办法：在Android Studio内找到File --> Project Structure 选中app，再点击右侧上方 Properties 修改Build Tools Version版本即可
