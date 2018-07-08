package jake.yang.dialog.library.utils;

import android.util.ArrayMap;

import jake.yang.dialog.library.CoreDialogActivity;


@SuppressWarnings("unused")
public class CoreDialogConstant {
    public static final int DEFAULT_REQUEST_CODE = 0x0001;
    public static final int DEFAULT_VALUE_CODE = 0;
    private static final ArrayMap<Integer, CoreDialogActivity> sActivitys = new ArrayMap<>();

    public static CoreDialogActivity getActivity(int requestCode) {
        return sActivitys.get(requestCode);
    }

    public static void putActivity(int requestCode, CoreDialogActivity activity) {
        sActivitys.put(requestCode, activity);
    }

    public static void destroyActivity(int requestCode) {
        sActivitys.remove(requestCode);
    }

    public static void destroyActivitys() {
        sActivitys.clear();
    }
}
