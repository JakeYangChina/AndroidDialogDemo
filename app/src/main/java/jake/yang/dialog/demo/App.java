package jake.yang.dialog.demo;

import android.app.Application;

import jake.yang.dialog.library.CoreDialog;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CoreDialog.init(this);
    }
}
