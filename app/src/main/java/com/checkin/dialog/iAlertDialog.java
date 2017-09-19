package com.checkin.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2015/8/3.
 */
public class iAlertDialog {

    private static AlertDialog alertDialog;

    private AlertDialog alertDialogAsync;
    private Context mContext;

    public iAlertDialog(Context mContext) {
        this.mContext = mContext;
    }

    public interface OnClickYesListener {
        public void onClickYes();
    }

    public interface OnClickNoListener {
        public void onClickNo();
    }

    public static void showAlertDialog(Context context, String content, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        iAlertDialog.showAlertDialog(context, "提示", content, "确认", "取消", listenerYes, listenerNo, true);
    }
    public static void showAlertMapDialog(Context context, String content, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        iAlertDialog.showAlertDialog(context, "提示", content, "覆盖", "仅设定坐标", listenerYes, listenerNo, true);
    }
    public static void showAlertDialog(Context context, String content, final OnClickYesListener listenerYes) {
        iAlertDialog.showAlertDialog(context, "提示", content, "确认", listenerYes, true);
    }
    public static void showSignAlertDialog(Context context, String content, final OnClickYesListener listenerYes) {
        iAlertDialog.showAlertDialog(context, "提示", content, "知道了", listenerYes, true);
    }

    public static void showAlertDialog(Context context, int contentResId, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        iAlertDialog.showAlertDialog(context, "提示", context.getString(contentResId), "确认", "取消", listenerYes, listenerNo, false);
    }
    public static void showAddressAlertDialog(Context context, int contentResId, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        iAlertDialog.showAlertDialog(context, "提示", context.getString(contentResId), "好的", "重新填写", listenerYes, listenerNo, false);
    }
    public static void showAlertDialog(Context context, int titleResId, int contentResId, int positiveStrResId, int nagativeStrResId, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        showAlertDialog(context, context.getString(titleResId), context.getString(contentResId), context.getString(positiveStrResId), context.getString(nagativeStrResId), listenerYes, listenerNo, false);
    }

    /**
     * @param context
     * @param title       标题
     * @param content     内容
     * @param positiveStr 确定按钮
     * @param nagativeStr 取消按钮
     * @param listenerYes 确定按钮事件
     * @param listenerNo  取消按钮事件
     */
    public static void showAlertDialog(Context context, String title, String content, String positiveStr, String nagativeStr, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(content);
        if (listenerYes != null) {
            builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listenerYes != null) {
                        listenerYes.onClickYes();
                    }
                }
            });
        }
        if (listenerNo != null) {
            builder.setNegativeButton(nagativeStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listenerNo.onClickNo();
                }
            });
        }
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(content);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (context != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    /**
     * @param context
     * @param title       标题
     * @param content     内容
     * @param positiveStr 确定按钮
     * @param nagativeStr 取消按钮
     * @param listenerYes 确定按钮事件
     * @param listenerNo  取消按钮事件
     */
    public static void showAlertDialog(Context context, String title, String content, String positiveStr, String nagativeStr, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        if (listenerYes != null) {
            builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listenerYes != null) {
                        listenerYes.onClickYes();
                    }
                }
            });
        }
        if (listenerNo != null) {
            builder.setNegativeButton(nagativeStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listenerNo.onClickNo();
                }
            });
        }
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (context != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void showAsyncAlertDialog(String title, String content, String positiveStr, String nagativeStr, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        if (alertDialogAsync == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Holo_Light_Dialog);
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(content);
            builder.setCancelable(false);
            builder.setTitle(title);
            builder.setMessage(content);
            if (listenerYes != null) {
                builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listenerYes != null) {
                            listenerYes.onClickYes();
                        }
                    }
                });
            }
            if (listenerNo != null) {
                builder.setNegativeButton(nagativeStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listenerNo.onClickNo();
                    }
                });
            }
            alertDialogAsync = builder.create();
            alertDialogAsync.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialogAsync.show();
        } else if (!alertDialogAsync.isShowing()) {
            alertDialogAsync.setCancelable(false);
            alertDialogAsync.setTitle(title);
            alertDialogAsync.setMessage(content);
            alertDialogAsync.setButton(DialogInterface.BUTTON_POSITIVE, positiveStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listenerYes != null) {
                        listenerYes.onClickYes();
                    }
                }
            });
            alertDialogAsync.setButton(DialogInterface.BUTTON_NEGATIVE, nagativeStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listenerYes != null) {
                        listenerNo.onClickNo();
                    }
                }
            });
            alertDialogAsync.show();
        }

    }

    public static void showAlertDialog(Context context, String title, String content, String positiveStr, final OnClickYesListener listenerYes, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(content);
        if (listenerYes != null) {
            builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listenerYes != null) {
                        listenerYes.onClickYes();
                    }
                }
            });
        }
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(content);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public static void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public void dismissDialogAsync() {
        if (alertDialogAsync != null && alertDialogAsync.isShowing()) {
            alertDialogAsync.dismiss();
        }
    }
}
