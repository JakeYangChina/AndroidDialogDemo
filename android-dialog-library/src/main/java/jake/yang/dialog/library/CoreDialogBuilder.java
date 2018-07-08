package jake.yang.dialog.library;

import android.view.View;

@SuppressWarnings("unused")
public class CoreDialogBuilder {

    private View mView;
    private int mTheme;
    private int mGravity;
    private boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private int mDelayTime;//指定activity延时关闭，与dialog动画时间保持一致
    private String mOrientation;
    private int mColorBg;

    private int mHorizontalMargin;
    private int mVerticalMargin;
    private String mLayoutWidthType;
    private String mLayoutHeightType;

    private String mTitle;
    private String mMessage;
    private String mOkName;
    private String mCancelName;
    private boolean mIsCanModifyLayout;
    private boolean mIsCancelableClick;
    private boolean mIsOnTouchOutsideClick;

    public CoreDialogBuilder setDelayTime(int delayTime) {
        this.mDelayTime = delayTime;
        return this;
    }

    public CoreDialogBuilder setGravity(int gravity) {
        this.mGravity = gravity;
        return this;
    }

    public CoreDialogBuilder setContentView(View view) {
        this.mView = view;
        return this;
    }

    public CoreDialogBuilder setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public CoreDialogBuilder setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public CoreDialogBuilder setOkButtonName(String okButtonName) {
        this.mOkName = okButtonName;
        return this;
    }

    public CoreDialogBuilder setNoButtonName(String noButtonName) {
        this.mCancelName = noButtonName;
        return this;
    }

    public CoreDialogBuilder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.mIsOnTouchOutsideClick = true;
        this.mCanceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public CoreDialogBuilder setCancelable(boolean cancelable) {
        this.mIsCancelableClick = true;
        this.mCancelable = cancelable;
        return this;
    }

    public CoreDialogBuilder setIsCanModifyLayout(boolean isCanModifyLayout) {
        this.mIsCanModifyLayout = isCanModifyLayout;
        return this;
    }

    public CoreDialogBuilder setOrientation(CoreInfo coreInfo) {
        this.mOrientation = coreInfo.name();
        return this;
    }


    public CoreDialogBuilder setRootViewBackgroundColor(int colorBg) {
        this.mColorBg = colorBg;
        return this;
    }

    /**
     * 设置dialog动画效果，通过配置style.xml主题
     * 需要继承Core.Theme.Dialog主题样式，覆写android:windowAnimationStyle条目，指定动画
     *
     * @param theme 样式
     * @return CoreDialogBuilder
     */
    public CoreDialogBuilder setAnimationStyle(int theme) {
        this.mTheme = theme;
        return this;
    }

    public CoreDialog build() {
        return new CoreDialog(
                mView,
                mTheme,
                mGravity,
                mDelayTime,
                mCancelable,
                mOrientation,
                mColorBg,
                mCanceledOnTouchOutside,
                mTitle, mMessage, mOkName, mCancelName, mIsCanModifyLayout,
                mIsCancelableClick, mIsOnTouchOutsideClick);
    }
}
