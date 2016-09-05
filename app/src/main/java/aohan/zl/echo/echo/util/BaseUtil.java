package aohan.zl.echo.echo.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Echo on 2016/8/31.
 */
public class BaseUtil {

    /**
     * 判断外置内存是否可以正常写入
     * @return
     */
    public static Boolean isExternalStorageWriteAble(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return  true;
        return  false;
    }

    /**
     * 判断外置内存是否可以正常读取
     * @return
     */
    public static Boolean isExternalStorageReadAble(){
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())||Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return  true;
        return  false;
    }


    /**
     * 显示一个只有确认按钮的消息提示框
     * @param context
     * @param message
     */
    public static   void  showCommonDialog(Context context,String message){
        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(context);
        alertdialogbuilder.setMessage(message);
        alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=alertdialogbuilder.create();
        dialog.show();
    }



}
