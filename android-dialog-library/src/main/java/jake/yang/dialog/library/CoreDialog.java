package jake.yang.dialog.library;

import android.app.Application;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.Method;
import java.util.Set;

import jake.yang.dialog.library.annotation.CoreDialogCancel;
import jake.yang.dialog.library.annotation.CoreDialogClickNo;
import jake.yang.dialog.library.annotation.CoreDialogClickOk;
import jake.yang.dialog.library.annotation.CoreDialogClose;
import jake.yang.dialog.library.annotation.CoreDialogShow;
import jake.yang.dialog.library.bean.CoreDialogObj;
import jake.yang.dialog.library.callback.CoreDialogCallback;
import jake.yang.dialog.library.utils.CoreDialogConstant;


/**
 * 对话框依赖于activity
 * 做成通用的对话框，无论是否在activity内都可用，使用透明Activity
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CoreDialog {
    //ProgressDialog.show(this, null, "请稍等...", true, false);
    //DialogFragment
    //静态方法里，不要将字段定义成静态，使用集合存储
    private static final ArrayMap<String, ArrayMap<Integer, CoreDialogObj>> sMap = new ArrayMap<>();
    static final ArrayMap<String, String> sOnly = new ArrayMap<>();
    static final SparseArray<Application> APPLICATION = new SparseArray<>();

    private View mView;
    private int mTheme;
    private int mGravity;
    private int mDelayTime;//指定activity延时关闭，与dialog动画时间保持一致
    private boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private String mOrientation;//是否横屏？
    private int mColorBg;

    private String mTitle;
    private String mMessage;
    private String mOkName;
    private String mCancelName;
    private boolean mIsCanModifyLayout;

    private boolean mIsCancelableClick;
    private boolean mIsOnTouchOutsideClick;

    public static void init(Application application) {
        APPLICATION.put(0, application);
    }

    CoreDialog(
            View view,
            int theme,
            int gravity,
            int delayTime,
            boolean cancelable,
            String orientation,
            int colorBg,
            boolean canceledOnTouchOutside,
            String title, String message,
            String okName, String cancelName, boolean isCanModifyLayout,
            boolean isCancelableClick, boolean isOnTouchOutsideClick) {

        this.mView = view;
        this.mTheme = theme;
        this.mGravity = gravity;
        this.mDelayTime = delayTime;
        this.mCancelable = !isCancelableClick || cancelable;
        this.mOrientation = orientation;
        this.mColorBg = colorBg;

        this.mTitle = title;
        this.mMessage = message;
        this.mOkName = okName;
        this.mCancelName = cancelName;
        this.mIsCanModifyLayout = isCanModifyLayout;
        this.mCanceledOnTouchOutside = !isOnTouchOutsideClick || canceledOnTouchOutside;

    }

    public void showDialog(Object curObj) {
        showDialog(curObj, CoreDialogConstant.DEFAULT_REQUEST_CODE);
    }

    public void showDialog(Object curObj, int requestCode) {
        check(curObj);
        Class<?> aClass = curObj.getClass();

        synchronized (sOnly) {
            if (sOnly.containsKey(aClass.getCanonicalName())) {
                return;
            }
            sOnly.put(aClass.getCanonicalName(), aClass.getCanonicalName());
        }

        ArrayMap<Integer, CoreDialogObj> arrayMap = sMap.get(aClass.getSimpleName());
        if (arrayMap == null) {
            arrayMap = new ArrayMap<>();
            String className = aClass.getCanonicalName();
            sMap.put(aClass.getSimpleName(), arrayMap);

            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(CoreDialogShow.class)) {
                    CoreDialogShow coreDialogShow = method.getAnnotation(CoreDialogShow.class);
                    apply(curObj, arrayMap, method, coreDialogShow,
                            null, null,
                            null, null, className);
                }

                if (method.isAnnotationPresent(CoreDialogClickOk.class)) {
                    CoreDialogClickOk coreDialogClickOk = method.getAnnotation(CoreDialogClickOk.class);

                    apply(curObj, arrayMap, method, null,
                            coreDialogClickOk, null,
                            null, null, className);
                }

                if (method.isAnnotationPresent(CoreDialogClickNo.class)) {
                    CoreDialogClickNo coreDialogClickNo = method.getAnnotation(CoreDialogClickNo.class);
                    apply(curObj, arrayMap, method, null,
                            null, coreDialogClickNo,
                            null, null, className);
                }

                if (method.isAnnotationPresent(CoreDialogClose.class)) {
                    CoreDialogClose coreDialogClose = method.getAnnotation(CoreDialogClose.class);
                    apply(curObj, arrayMap, method, null,
                            null, null,
                            coreDialogClose, null, className);
                }

                if (method.isAnnotationPresent(CoreDialogCancel.class)) {
                    CoreDialogCancel coreDialogCancel = method.getAnnotation(CoreDialogCancel.class);
                    apply(curObj, arrayMap, method, null,
                            null, null,
                            null, coreDialogCancel, className);
                }
            }
        }

        Set<Integer> keySet = arrayMap.keySet();
        if (keySet.isEmpty()) {
            CoreDialogObj obj = new CoreDialogObj();
            obj.mRequestCode = -1;
            obj.mClassName = aClass.getCanonicalName();
            startDialog(obj);
            return;
        }


        for (int key : keySet) {
            if (key == requestCode) {
                CoreDialogObj coreDialogObj = arrayMap.get(key);
                startDialog(coreDialogObj);
                return;
            }
        }
    }

    private void apply(
            Object curObj,
            ArrayMap<Integer, CoreDialogObj> arrayMap,
            Method method,
            CoreDialogShow coreDialogShow,
            CoreDialogClickOk coreDialogClickOk,
            CoreDialogClickNo coreDialogClickNo,
            CoreDialogClose coreDialogClose,
            CoreDialogCancel coreDialogCancel,
            String className) {

        if (coreDialogShow != null) {
            method.setAccessible(true);
            int code = coreDialogShow.requestCode();
            CoreDialogObj obj = arrayMap.get(code);
            if (obj == null) {
                obj = new CoreDialogObj();
                arrayMap.put(code, obj);
            }
            obj.applyShow(method, code, curObj, className);
        }

        if (coreDialogClickOk != null) {
            method.setAccessible(true);
            int code = coreDialogClickOk.requestCode();
            CoreDialogObj obj = arrayMap.get(code);
            if (obj == null) {
                obj = new CoreDialogObj();
                arrayMap.put(code, obj);
            }
            obj.applyClickOk(method, code, curObj, coreDialogClickOk.id(), className);
        }

        if (coreDialogClickNo != null) {
            method.setAccessible(true);
            int code = coreDialogClickNo.requestCode();
            CoreDialogObj obj = arrayMap.get(code);
            if (obj == null) {
                obj = new CoreDialogObj();
                arrayMap.put(code, obj);
            }
            obj.applyClickNo(method, code, curObj, coreDialogClickNo.id(), className);
        }

        if (coreDialogClose != null) {
            method.setAccessible(true);
            int code = coreDialogClose.requestCode();
            CoreDialogObj obj = arrayMap.get(code);
            if (obj == null) {
                obj = new CoreDialogObj();
                arrayMap.put(code, obj);
            }
            obj.applyClose(method, code, curObj, className);
        }

        if (coreDialogCancel != null) {
            method.setAccessible(true);
            int code = coreDialogCancel.requestCode();
            CoreDialogObj obj = arrayMap.get(code);
            if (obj == null) {
                obj = new CoreDialogObj();
                arrayMap.put(code, obj);
            }
            obj.applyCancel(method, code, curObj, className);
        }
    }

    private void startDialog(final CoreDialogObj coreDialogObj) {
        CoreDialogActivity.start(
                CoreDialog.APPLICATION.get(0).getApplicationContext(),
                mView,
                mDelayTime,
                mCancelable,
                mCanceledOnTouchOutside,
                coreDialogObj.mClickOkId,
                coreDialogObj.mClickNoId,
                mTheme, mGravity, coreDialogObj.mRequestCode, mOrientation,
                mColorBg,
                coreDialogObj.mClassName,
                mTitle, mMessage, mOkName, mCancelName, mIsCanModifyLayout,
                new CoreDialogCallback() {
                    @Override
                    public void onShow() {

                        try {
                            coreDialogObj.mShowMethod.invoke(coreDialogObj.mObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClose() {
                        try {
                            coreDialogObj.mCloseMethod.invoke(coreDialogObj.mObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancel() {
                        try {
                            coreDialogObj.mCancelMethod.invoke(coreDialogObj.mObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClickOk() {
                        try {
                            coreDialogObj.mClickOkMethod.invoke(coreDialogObj.mObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClickNo() {
                        try {
                            coreDialogObj.mClickNoMethod.invoke(coreDialogObj.mObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void check(Object curObj) {
        if (curObj == null)
            throw new RuntimeException("curObj is null.");
    }

    public static void closeDialog(Object curObj) {
        closeDialog(curObj, CoreDialogConstant.DEFAULT_REQUEST_CODE);
    }

    public static void closeDialog(Object curObj, int requestCode) {
        CoreDialogActivity activity = CoreDialogConstant.getActivity(requestCode);
        if (activity == null)
            return;

        ArrayMap<Integer, CoreDialogObj> arrayMap = sMap.get(curObj.getClass().getSimpleName());
        if (arrayMap != null && !arrayMap.isEmpty() && !activity.isFinishing()) {
            CoreDialogConstant.destroyActivity(requestCode);
            activity.closeDialog();
        }

    }

    public static void destroyDialog(Object curObj) {
        if (sMap.isEmpty())
            return;
        ArrayMap<Integer, CoreDialogObj> arrayMap = sMap.get(curObj.getClass().getSimpleName());
        destroy(arrayMap);
    }

    private static void destroy(ArrayMap<Integer, CoreDialogObj> arrayMap) {
        if (arrayMap != null && !arrayMap.isEmpty()) {
            Set<Integer> keySet = arrayMap.keySet();
            for (int key : keySet) {
                CoreDialogObj coreDialogObj = arrayMap.get(key);
                coreDialogObj.clear();
            }
            keySet.clear();
            arrayMap.clear();
        }

        APPLICATION.clear();
    }

    public static void destroyDialogAll() {
        if (!sMap.isEmpty()) {
            Set<String> keySet = sMap.keySet();
            for (String key : keySet) {
                ArrayMap<Integer, CoreDialogObj> arrayMap = sMap.get(key);
                if (arrayMap.isEmpty()) {
                    continue;
                }
                destroy(arrayMap);
            }
            sMap.clear();
        }
    }

    public static CoreDialogBuilder builder() {
        return new CoreDialogBuilder();
    }

}
