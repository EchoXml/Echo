package aohan.zl.echo.echo;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSelectActivity extends AppCompatActivity {

    //应用内置的存储路径
    private File mPrivateRootDir;
    //图片所在路径
    private File mImagesDir;
    //图片所在路径的图片数组
    File[] mImageFiles;
    //图片名字数组
    String[] mImageFileNames;

    private Intent mResultIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mResultIntent=new Intent(getPackageName()+".ACTION_RETURN_FILE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        initData();
        setResult(RESULT_CANCELED,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_finish,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemFinish:
                finish();
                break;
        }
        return true;
    }

    /**
     * 初始化相关数据
     */
    private void initData(){
        mPrivateRootDir=getFilesDir();
        mImagesDir=new File(mPrivateRootDir,"images");
        mImageFiles=mImagesDir.listFiles();
        List<Map<String,Object>> list=new ArrayList<>();

        for (File file:mImageFiles) {
            HashMap<String,Object> map=new HashMap<>();
            map.put("fileName",file.getName());
            map.put("wholeName",file.getAbsolutePath());
            map.put("bitmap", file);
            map.put("size",file.length());
            list.add(map);
        }

        final ListView photos= (ListView) findViewById(R.id.lvShowImages);

        System.out.println(photos==null);
        SimpleAdapter simpleAdapter=new SimpleAdapter(getApplicationContext(),list,R.layout.activity_fileselect_list_view,new String[]{"bitmap","fileName","size"},
                new int[]{R.id.ivImage,R.id.tvImageName,R.id.tvImageSize});

        photos.setAdapter(simpleAdapter);
        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                LinearLayout linearLayout= photos.getChildAt(position).invalidate();
              //  TextView tv=(TextView)lv.getChildAt(0).findViewById(R.id.friend_msgNum_tv);
                TextView textView= (TextView) photos.getChildAt(position).findViewById(R.id.tvImageName);
                Toast.makeText(getApplicationContext(),textView.getText(),Toast.LENGTH_SHORT).show();
                //获取对于未知的文件
                File mFile=mImageFiles[position];
                //该文件对于的Url
                Uri fileUri=null;
                try{
                    fileUri= FileProvider.getUriForFile(getApplicationContext(),getPackageName()+".fileprovider",mFile);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                //create intent and grant temp permission
                if (fileUri!=null){
                    //The permission will expire when the app who receive the file over
                    mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                mResultIntent.setDataAndType(fileUri,getContentResolver().getType(fileUri));
                setResult(RESULT_OK,mResultIntent);

            }
        });
    }
}
