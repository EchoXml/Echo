package aohan.zl.echo.echo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aohan.zl.echo.echo.dao.MySQLite;
import aohan.zl.echo.echo.model.User;
import aohan.zl.echo.echo.util.BaseUtil;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(this.getClass().getName()+" onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btnToMenu= (Button) findViewById(R.id.btn_ToMenu);

        if (btnToMenu != null) {
            btnToMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"功能未实现！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        System.out.println("onStart...");
        super.onStart();
        Intent intent=getIntent();
        if (intent.getType()!=null&&intent.getType().equals("text/plain")){
            TextView show= (TextView) findViewById(R.id.tvShowSp);
            show.setText(intent.getExtras().get(Intent.EXTRA_TEXT).toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 执行登录操作
     * @param view
     */
    public  void doLogin(View view){
        EditText userName=(EditText) this.findViewById(R.id.etUserName);
        EditText pwd=(EditText) this.findViewById(R.id.etPwd);
        if (userName != null) {
            if (userName.getText().length()==0||pwd.getText().length()==0) {
                Toast.makeText(getApplicationContext(),"用户名与密码不可为空！",Toast.LENGTH_SHORT).show();
            }else{
                User user=new User(null, userName.getText().toString(),pwd.getText().toString());
                //MODE_PRIVATE表明该文件只能被我们自己的app访问，不能被其他app访问，反之，MODE_READABLE和MODE_WRITEABLE则可以
                SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString(user.getUserName(),user.getPassword());
                editor.commit();
                BaseUtil.showCommonDialog(getApplicationContext(),"成功添加信息到sharedPreferences!");
            }
        }
    }

    public  void doReset(View view){
        EditText userName=(EditText) this.findViewById(R.id.etUserName);
        EditText pwd=(EditText) this.findViewById(R.id.etPwd);
        userName.setText("");
        pwd.setText("");

    }

    /**
     * 读Shared Preference
     * @param view
     */
    public  void  showSp(View view){
        SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
        EditText userName=(EditText) findViewById(R.id.etSearchKey);
        String pwd=sp.getString(userName.getText().toString(),"未找到相应内容，现有"+sp.getAll().size()+"条对应记录");
        TextView textView=(TextView)findViewById(R.id.tvShowSp);
        textView.setText(pwd);

    }

    public void queryByName(View view){
        MySQLite mySQLite=new MySQLite(getApplicationContext());
        SQLiteDatabase db=mySQLite.getWritableDatabase();
        String[] projection=new String[]{MySQLite.COLUMN_NAME_USERID,MySQLite.COLUMN_NAME_USERNAME,MySQLite.COLUMN_NAME_PASSWORD};
        //根据某列进行排序
        String sortOrder=MySQLite.COLUMN_NAME_USERID;
        Cursor cursor=db.rawQuery("select * from "+MySQLite.TABLE_NAME,null);
        List<User> userList=new ArrayList<>();
        while (cursor.moveToNext()){
            User user=new User(cursor.getInt(cursor.getColumnIndex(projection[0])),cursor.getString(cursor.getColumnIndex(projection[1])),cursor.getString(cursor.getColumnIndex(projection[2])));
            userList.add(user);
        }
        cursor.close();
        db.close();
        TextView textView=(TextView)findViewById(R.id.tvShowSp);
        StringBuffer sb=new StringBuffer();
        for (User user:userList
             ) {
            sb.append("编号："+user.getUserId()+" 用户名："+user.getUserName()+" 密码："+user.getPassword());
        }
        textView.setText(sb.toString());
    }

    /**
     * 将SharedPreferences内的内容写入到文件中
     * @param view
     */
    public Boolean  writeSpTofile(View view){
        File file=new File(getExternalStorageDir("txtDemo"),"spInfo.txt");
        FileOutputStream fos=null;
        StringBuffer sb=new StringBuffer();
        System.out.print(file.getName());
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {

                SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
                Map<String,String> map= (Map<String, String>) sp.getAll();
                for (String key:map.keySet() ) {
                    sb.append("用户名："+key+",密码："+map.get(key)+"\n");
                }
                fos=new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                fos.flush();

             BaseUtil.showCommonDialog(getApplicationContext(),"总容量："+file.getTotalSpace()/1024.0/1024/1024+"GB");
            BaseUtil.showCommonDialog(getApplicationContext(),"可用空间："+file.getFreeSpace()/1024.0/1024/1024+"GB");

            BaseUtil.showCommonDialog(getApplicationContext(),"成功将"+map.size()+"条记录写入文件。");
                return  true;

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



         return  false;
    }

    public File getExternalStorageDir(String albumName){
        if (BaseUtil.isExternalStorageWriteAble()){
            File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),albumName);
             return  file;
        }
        return  null;
    }

    public void showTest(View view){
        MySQLite mySQLite=new MySQLite(getApplicationContext());
        SQLiteDatabase db=mySQLite.getWritableDatabase();
        ContentValues values=new ContentValues();
        SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
        Map<String,String> map= (Map<String, String>) sp.getAll();
        for (String key:map.keySet() ) {
           values.put(MySQLite.COLUMN_NAME_USERNAME,key);
           values.put(mySQLite.COLUMN_NAME_PASSWORD,map.get(key));
            db.insert(MySQLite.TABLE_NAME,null,values);
        }


    }

    public void toIntentActivity(View view){
        Intent intent=new Intent(this,IntentDemoActivity.class);
        startActivity(intent);
    }

}
