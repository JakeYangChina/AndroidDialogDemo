package jake.yang.dialog.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jake.yang.dialog.library.callback.CoreDialogCallback;
import jake.yang.dialog.library.utils.CoreDialogConstant;


public class CoreDialogActivity extends AppCompatActivity implements Runnable,
        DialogInterface.OnCancelListener, DialogInterface.OnDismissListener,
        DialogInterface.OnShowListener, View.OnClickListener {

    private static CoreDialogCallback sCallback;
    private static ArrayList<View> sViews = new ArrayList<>();
    private static ArrayList<Handler> sHandler = new ArrayList<>();

    private static final String DELAY_TIME = "delay_time";
    private static final String REQUEST_CODE = "request_code";
    private static final String CANCELABLE = "cancelable";
    private static final String CANCELED_TOUCH_OUTSIDE = "canceled_touch_outside";
    private static final String THEME = "theme";
    private static final String GRAVITY = "gravity";
    private static final String CLICK_OK_ID = "click_ok_id";
    private static final String CLICK_NO_ID = "click_no_id";
    private static final String ORIENTATION = "orientation";
    private static final String COLOR_BACKGROUND = "color_background";
    private static final String CLASS_NAME = "class_name";

    private static final int sDelayTime = 0;//延时时间关闭activity，这个时间与dialog动画时间保持一致

    private int mDelayTime;//指定activity延时关闭，与dialog动画时间保持一致
    private @IdRes
    int mClickOkId;
    private @IdRes
    int mClickNoId;
    int mTheme;
    int mGravity;
    int mRequestCode;

    private boolean mCancelable;
    private boolean mCanceledOnTouchOutside;
    private AlertDialog mDialog;
    private int mColorBg;

    private String mClassName;

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String OK_NAME = "ok_name";
    private static final String CANCEL_NAME = "cancel_name";
    private static final String IS_CAN_MODIFY_LAYOUT = "isCanModifyLayout";

    private String mTitle;
    private String mMessage;
    private String mOkName;
    private String mCancelName;
    private boolean mIsCanModifyLayout;

    public static void start(
            Context context,
            View dialogLayout,
            int delayTime,
            boolean cancelable,
            boolean canceledOnTouchOutside,
            @IdRes int clickOkId,
            @IdRes int clickNoId,
            int theme,
            int gravity,
            int requestCode,
            String orientation,
            int colorBg,
            String className,
            String title, String message, String okName, String cancelName, boolean isCanModifyLayout,
            CoreDialogCallback callback) {

        sCallback = callback;
        Intent intent = new Intent(context, CoreDialogActivity.class);

        putExtra(delayTime, cancelable, canceledOnTouchOutside,
                clickOkId, clickNoId, theme, gravity, requestCode,
                orientation, colorBg, intent, className,
                title, message, okName, cancelName, isCanModifyLayout);

        sViews.clear();
        if (dialogLayout != null)
            sViews.add(dialogLayout);

        if (sHandler != null) {
            sHandler.clear();
            sHandler.add(new Handler(Looper.getMainLooper()));
        }

        context.startActivity(intent);
        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).overridePendingTransition(0, 0);
        }

    }

    private static void putExtra(
            int delayTime, boolean cancelable, boolean canceledOnTouchOutside,
            @IdRes int clickOkId, @IdRes int clickNoId, int theme, int gravity,
            int requestCode, String orientation,
            int colorBg, Intent intent, String className,
            String title, String message, String okName, String cancelName, boolean isCanModifyLayout) {
        intent.putExtra(THEME, theme);
        intent.putExtra(CLASS_NAME, className);
        intent.putExtra(REQUEST_CODE, requestCode);
        intent.putExtra(GRAVITY, gravity);
        intent.putExtra(COLOR_BACKGROUND, colorBg);
        intent.putExtra(CLICK_OK_ID, clickOkId);
        intent.putExtra(CLICK_NO_ID, clickNoId);
        intent.putExtra(DELAY_TIME, delayTime);
        intent.putExtra(CANCELABLE, cancelable);
        intent.putExtra(ORIENTATION, orientation);
        intent.putExtra(CANCELED_TOUCH_OUTSIDE, canceledOnTouchOutside);

        intent.putExtra(TITLE, title);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(OK_NAME, okName);
        intent.putExtra(CANCEL_NAME, cancelName);
        intent.putExtra(IS_CAN_MODIFY_LAYOUT, isCanModifyLayout);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreStatusBar.theme(this, R.style.Core_Theme_Dialog_Status);
        Intent intent = getIntent();
        getExtra(intent);
        setContentView(R.layout.core_dialog_layout_activity);
        CoreStatusBar.statusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));

        CoreDialogConstant.putActivity(mRequestCode, this);
        getWindow().getDecorView().post(this);
    }


    private void getExtra(Intent intent) {

        this.mTheme = intent.getIntExtra(THEME, 0);
        int code = intent.getIntExtra(REQUEST_CODE, -1);
        this.mRequestCode = code == -1 ? CoreDialogConstant.DEFAULT_REQUEST_CODE : code;
        this.mGravity = intent.getIntExtra(GRAVITY, 0);
        this.mClickOkId = intent.getIntExtra(CLICK_OK_ID, 0);
        this.mClickNoId = intent.getIntExtra(CLICK_NO_ID, 0);
        int intExtra = intent.getIntExtra(DELAY_TIME, 0);
        this.mDelayTime = intExtra == 0 ? sDelayTime : intExtra;
        this.mCancelable = intent.getBooleanExtra(CANCELABLE, false);
        this.mColorBg = intent.getIntExtra(COLOR_BACKGROUND, 0);

        this.mTitle = intent.getStringExtra(TITLE);
        this.mMessage = intent.getStringExtra(MESSAGE);
        this.mOkName = intent.getStringExtra(OK_NAME);
        this.mCancelName = intent.getStringExtra(CANCEL_NAME);
        this.mIsCanModifyLayout = intent.getBooleanExtra(IS_CAN_MODIFY_LAYOUT, false);

        this.mClassName = intent.getStringExtra(CLASS_NAME);

        String orientation = intent.getStringExtra(ORIENTATION);
        this.mCanceledOnTouchOutside = intent.getBooleanExtra(CANCELED_TOUCH_OUTSIDE, false);
        if (CoreInfo.SCREEN_ORIENTATION_LANDSCAPE.name().equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (CoreInfo.SCREEN_ORIENTATION_PORTRAIT.name().equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ConstantConditions")
    @Override
    public void run() {

        AlertDialog.Builder builder;

        if (this.mIsCanModifyLayout) {
            builder = new AlertDialog.Builder(this, R.style.Core_Theme_Dialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        if (sViews.isEmpty()) {
            if (!TextUtils.isEmpty(mTitle)) {
                builder.setTitle(mTitle);
            }

            if (!TextUtils.isEmpty(mMessage)) {
                builder.setMessage(mMessage);
            }

            if (!TextUtils.isEmpty(mCancelName)) {
                builder.setNegativeButton(mCancelName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (sCallback != null) {
                            sCallback.onClickNo();
                        }
                    }
                });
            }

            if (!TextUtils.isEmpty(mOkName)) {
                builder.setPositiveButton(mOkName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (sCallback != null) {
                            sCallback.onClickOk();
                        }
                    }
                });
            }

        } else {
            View view = sViews.get(0);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }

            if (view instanceof ViewGroup) {
                View v = ((ViewGroup) view).getChildAt(0);
                if (v != null) {
                    v.setBackgroundColor(mColorBg == 0 ? Color.WHITE : mColorBg);
                }
            }

            if (this.mClickOkId != 0)
                view.findViewById(this.mClickOkId).setOnClickListener(this);
            if (this.mClickNoId != 0)
                view.findViewById(this.mClickNoId).setOnClickListener(this);
            //mView.addView(view);
            builder.setView(view);
        }


        //builder.
        mDialog = builder.create();
        if (mTheme != 0)
            mDialog.getWindow().setWindowAnimations(mTheme);
        mDialog.setOnShowListener(this);
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        mDialog.setCancelable(mCancelable);
        mDialog.setOnDismissListener(this);
        mDialog.setOnCancelListener(this);
        mDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDialog();

        synchronized (CoreDialog.sOnly) {
            CoreDialog.sOnly.remove(this.mClassName);
        }

        if (sViews != null) {
            sViews.clear();
        }

        if (sHandler != null) {
            sHandler.clear();
        }

        CoreDialogConstant.destroyActivity(mRequestCode);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (sCallback != null) {
            sCallback.onCancel();
        }
    }

    private void check() {
        if (!isFinishing() && !sHandler.isEmpty()) {
            if (!isFinishing()) {
                sHandler.get(0).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing() && !sHandler.isEmpty()) {
                            sHandler.get(0).removeCallbacksAndMessages(null);
                            if (sCallback != null) {
                                sCallback.onClose();
                            }
                            finish();
                        }
                    }
                }, mDelayTime);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        check();
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        if (sCallback != null) {
            sCallback.onShow();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == this.mClickOkId) {
            if (sCallback != null) {
                sCallback.onClickOk();
            }
        } else {
            if (sCallback != null) {
                sCallback.onClickNo();
            }
        }
        closeDialog();
    }

    void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
