package jake.yang.dialog.library.callback;

@SuppressWarnings("unused")
public interface CoreDialogCallback {
    void onShow();

    void onClose();

    void onCancel();

    void onClickOk();

    void onClickNo();
}
