package jake.yang.dialog.library.bean;

import java.lang.reflect.Method;

public class CoreDialogObj {
    public Method mCancelMethod;
    public Method mCloseMethod;
    public Method mClickOkMethod;
    public Method mClickNoMethod;
    public Method mShowMethod;
    public Object mObject;
    public int mRequestCode;
    public int mClickOkId;
    public int mClickNoId;
    public String mClassName;

    public void clear() {
        this.mCancelMethod = null;
        this.mCloseMethod = null;
        this.mClickOkMethod = null;
        this.mClickNoMethod = null;
        this.mShowMethod = null;
        this.mObject = null;
    }

    public void applyShow(Method method, int code, Object curObj, String className) {
        this.mShowMethod = method;
        this.mRequestCode = code;
        this.mObject = curObj;
        this.mClassName = className;
    }

    public void applyClickOk(Method method, int code, Object curObj, int clickOkId, String className) {
        this.mClickOkMethod = method;
        this.mRequestCode = code;
        this.mObject = curObj;
        this.mClickOkId = clickOkId;
        this.mClassName = className;
    }

    public void applyClickNo(Method method, int code, Object curObj, int clickNoId, String className) {
        this.mClickNoMethod = method;
        this.mRequestCode = code;
        this.mObject = curObj;
        this.mClickNoId = clickNoId;
        this.mClassName = className;
    }

    public void applyClose(Method method, int code, Object curObj, String className) {
        this.mCloseMethod = method;
        this.mRequestCode = code;
        this.mObject = curObj;
        this.mClassName = className;
    }

    public void applyCancel(Method method, int code, Object curObj, String className) {
        this.mCancelMethod = method;
        this.mRequestCode = code;
        this.mObject = curObj;
        this.mClassName = className;
    }
}
