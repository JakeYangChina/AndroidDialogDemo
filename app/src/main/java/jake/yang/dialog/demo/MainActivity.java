package jake.yang.dialog.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import jake.yang.dialog.library.CoreDialog;
import jake.yang.dialog.library.annotation.CoreDialogCancel;
import jake.yang.dialog.library.annotation.CoreDialogClickNo;
import jake.yang.dialog.library.annotation.CoreDialogClickOk;
import jake.yang.dialog.library.annotation.CoreDialogClose;
import jake.yang.dialog.library.annotation.CoreDialogShow;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void show(View view) {
        CoreDialog.builder()
                .setCanceledOnTouchOutside(false)
                .setContentView(View.inflate(this, R.layout.default_dialog, null))
                .build()
                .showDialog(this, 2);
    }

    public void close(View view) {
        CoreDialog.closeDialog(this, 2);
    }

    @CoreDialogShow(requestCode = 2)
    void dialogShow() {
        Log.e(TAG, "dialogShow");
    }

    @CoreDialogClose(requestCode = 2)
    void dialogClose() {
        Log.e(TAG, "dialogClose");
    }

    @CoreDialogCancel(requestCode = 2)
    void dialogCancel() {
        Log.e(TAG, "dialogCancel");
    }

    @CoreDialogClickOk(requestCode = 2, id = R.id.dialogClickOk)
    void dialogClickOk() {
        Log.e(TAG, "dialogClickOk");
    }

    @CoreDialogClickNo(requestCode = 2, id = R.id.dialogClickNo)
    void dialogClickNo() {
        Log.e(TAG, "dialogClickNo");
    }
}
