package aohan.zl.echo.echo;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        initData();
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
            }
        });
    }
}
