package aohan.zl.echo.echo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Paint;


/**
 * 数据库连接操作类
 * Created by Echo on 2016/8/30.
 */
public class MySQLite extends SQLiteOpenHelper {

    private  static final Integer DATABASE_VERSION=1;

    private static final String DATABASE_NAME="userInfo.db";

    public static  final String TABLE_NAME="user_info";

    public static  final String COLUMN_NAME_USERID="userId";

    public static  final String COLUMN_NAME_USERNAME="userName";

    public static  final String COLUMN_NAME_PASSWORD="password";

    private static final String TEXT_TYPE = " TEXT";

    private  static final String  SQL_CREATE_TABLE="create table "+TABLE_NAME+"("+COLUMN_NAME_USERID+" integer primary key autoincrement,"
            +COLUMN_NAME_USERNAME+ TEXT_TYPE+","+COLUMN_NAME_PASSWORD+TEXT_TYPE+" )";

    //构造函数,接收上下文作为参数,直接调用的父类的构造函数  
    public MySQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

    }

    public MySQLite(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           // db.execSQL();
    }
}
