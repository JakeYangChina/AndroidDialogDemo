package jake.yang.dialog.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jake.yang.dialog.library.utils.CoreDialogConstant;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CoreDialogClickOk {
    int id() default CoreDialogConstant.DEFAULT_VALUE_CODE;
    int requestCode() default CoreDialogConstant.DEFAULT_REQUEST_CODE;
}
