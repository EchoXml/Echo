package aohan.zl.echo.echo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import aohan.zl.echo.echo.model.ContactPerson;
import aohan.zl.echo.echo.util.BaseUtil;

public class IntentDemoActivity extends AppCompatActivity {

    private  Activity thisActivity=null;

    private String willCallPhone="10000";

    private static final Integer CONTACT_REQUEST_CODE=100;

    private static final Integer CONTACTHEAD_REQUEST_CODE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_demo);
        thisActivity=this;
    }

    public void toCall(View view) {
        Uri uri = Uri.parse("tel:18966170721");
        Intent call = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(call);
    }

    public void showHead(View view){
        Intent pickContactPerson = new Intent(Intent.ACTION_PICK, Uri.parse("Content://contacts"));
        pickContactPerson.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactPerson, CONTACTHEAD_REQUEST_CODE);
    }

    public void toShare(View view) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
//        share.putExtra(Intent.EXTRA_TITLE,"标题");
        share.putExtra(Intent.EXTRA_TEXT, "你好，这是通过Intent分享的一段文本");
//        share.putExtra(Intent.EXTRA_SUBJECT,"主题");
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(new Intent(Intent.createChooser(share, "分享功能测试")));
    }

    public void toMap(View view) {

//            Uri uri=Uri.parse("intent://map/line?coordtype=&zoom=&region=上海&name=28&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
//        ///intent = Intent.getIntent("intent://map/line?coordtype=&zoom=&region=上海&name=28&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
//            Intent map=new Intent(Intent.ACTION_VIEW,uri);
//            PackageManager manager=getPackageManager();
//            boolean isIntentSafe=manager.queryIntentActivities(map,0).size()>0;
//             showCommonDialog("是否存在对应app接收意图？"+isIntentSafe);
//          startActivity(map);
        Uri location = Uri.parse("baidumap://map/direction?origin=lating:30.4287940000,120.2560860000|name:临平大道797号&destination=临平地铁站&mode=transit&region=杭州");
// Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);

    }

    public void toWeb(View view) {
        Uri uri = Uri.parse("https://www.163.com");
        Intent call = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(call.createChooser(call, "打开网站"));
    }

    public void toMail(View view) {
        //Uri uri=Uri.parse("tel:18966170721");
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(getExternalStorageDir("txtDemo"), "spInfo.txt");
        if (file.getName().endsWith(".gz")) {
            emailIntent.setType("application/x-gzip");
        } else if (file.getName().endsWith(".txt")) {
            emailIntent.setType("text/plain");
        } else {
            emailIntent.setType("application/octet-stream");
        }
        //添加附件
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Intent发送邮件");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "这是一封测试邮件");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"t95921@gmail.com"});

        startActivity(emailIntent.createChooser(emailIntent, "Send Email"));
    }

    public void toCalendar(View view) {
        Intent calendar = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2016, 8, 31, 12, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2016, 8, 31, 18, 0);
        calendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendar.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendar.putExtra(CalendarContract.Events.TITLE, "事件测试");
        calendar.putExtra(CalendarContract.Events.EVENT_LOCATION, "酒球会");
        startActivity(calendar);

    }

    public void toContact(View view) {
        Intent pickContactPerson = new Intent(Intent.ACTION_PICK, Uri.parse("Content://contacts"));
        pickContactPerson.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactPerson,CONTACT_REQUEST_CODE);
    }

    /**
     * 当用户完成了启动之后activity操作之后，系统会调用我们activity中的onActivityResult() 回调方法。该方法有三个参数：
     通过startActivityForResult()传递的request code。
     第二个activity指定的result code。如果操作成功则是RESULT_OK ，如果用户没有操作成功，而是直接点击回退或者其他什么原因，那么则是RESULT_CANCELED
     包含了所返回result数据的intent。
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK&&requestCode==CONTACT_REQUEST_CODE) {
            Uri contact = data.getData();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Contacts.Photo.PHOTO_ID};
            Cursor cursor = getContentResolver().query(contact, projection, null, null, null);
            cursor.moveToNext();
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            Integer contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            Integer photoId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_ID));
            Bitmap photo = null;
            ContactPerson person = new ContactPerson(contactName, phoneNumber, contactId, photoId, photo);
            Gson gson = new Gson();
            cursor.close();
        }else if (resultCode==RESULT_OK&&requestCode==CONTACTHEAD_REQUEST_CODE){
            Uri contact = data.getData();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Contacts.Photo.PHOTO_ID};

            Cursor cursor = getContentResolver().query(contact, projection, null, null, null);
            cursor.moveToNext();
            Integer photoId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_ID));
           long contactId=(long)cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            Bitmap photo = null;
            // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
            if (photoId>0){

               // byte[] head=cursor.getBlob(cursor.getColumnIndex(ContactsContract.Data.DATA15));
                InputStream inputStream=null;

                try {
                    Uri uri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
                    inputStream=ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),uri,true);
                }catch (Exception e){
                    e.printStackTrace();
                }
                photo= BitmapFactory.decodeStream(inputStream);
//                ImageView photoIv= (ImageView) findViewById(R.id.ivHead);
//                photoIv.setImageBitmap(photo);
                //OutputStream outputStream=new FileOutputStream(new File(photo))
                Intent photoIntent=new Intent();
                photoIntent.putExtra("photo",photo);
                setResult(resultCode,photoIntent);
                return;
            }
        }
    }

    public void getAllContact(View view) {
        //动态申请权限
        //如果未获得对应权限，则申请授权
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            try {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 11);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            showAllContact();
        }


    }

    /**
     * 显示所有联系人到列表
     */
    public void showAllContact(){
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.Contacts.Photo.PHOTO_ID};
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        final List<ContactPerson> persons = new ArrayList<>();
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            Integer contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            Integer photoId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_ID));
            Bitmap photo = null;
            //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
//            if (photoId>0){
//
//                InputStream inputStream=ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),uri);
//                photo= BitmapFactory.decodeStream(inputStream);
//            }
            ContactPerson person = new ContactPerson(contactName, phoneNumber, contactId, photoId, photo);
            persons.add(person);
        }

        final ListView contacts = (ListView) findViewById(R.id.lvShowContacts);


        List<HashMap<String, Object>> list = new ArrayList<>();

        for (ContactPerson person : persons) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("contactId", person.getContactId());
            map.put("contactName", person.getContactName());
            map.put("phoneNumber", person.getPhoneNumber());
            map.put("photoId", person.getPhotoId());
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.activity_contact_list_view, new String[]{"contactName", "phoneNumber"},
                new int[]{R.id.tvContactName, R.id.tvPhoneNumber});
        contacts.setAdapter(adapter);
        contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = contacts.getItemAtPosition(position).toString();
                willCallPhone= content.substring(content.lastIndexOf("phoneNumber=") + 12, content.lastIndexOf("}"));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        try{
                            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,Manifest.permission.CALL_PHONE)){
                                Toast.makeText(getApplicationContext(),"没有该权限无法进行直接拨号。",Toast.LENGTH_SHORT);
                            }else {
                                ActivityCompat.requestPermissions(thisActivity, new String[]{android.Manifest.permission.CALL_PHONE}, 111);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        callPhone(willCallPhone);
                    }
                } else {
                    Uri tel = Uri.parse("tel:" + willCallPhone);
                    Intent telIntent = new Intent(Intent.ACTION_DIAL, tel);
                    startActivity(telIntent);
                }


            }
        });
    }


    public void callPhone(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNumber)));
    }

    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public boolean checkInstallation(Context context,String packageName){
            //获取packagemanager
            final PackageManager packageManager = context.getPackageManager();
            //获取所有已安装程序的包信息
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            //用于存储所有已安装程序的包名
            List<String> packageNames = new ArrayList<String>();
            //从pinfo中将包名字逐一取出，压入pName list中
            if(packageInfos != null){
                for(int i = 0; i < packageInfos.size(); i++){
                    String packName = packageInfos.get(i).packageName;
                    packageNames.add(packName);
                }
            }
            //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
            return packageNames.contains(packageName);
    }

    /**
     * 验证是否有App去接收这个Intent
     * Caution: 如果触发了一个intent，而且没有任何一个app会去接收这个intent，则app会crash
     * @param intent
     * @return
     */
    public boolean checkIntentActivites(Intent intent){
        PackageManager manager=getPackageManager();
        return  manager.queryIntentActivities(intent,0).size()>0;
    }

    public  void  showCommonDialog(String message){
        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(this);
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

    public File getExternalStorageDir(String albumName){
        if (BaseUtil.isExternalStorageWriteAble()){
            File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),albumName);
            return  file;
        }
        return  null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 11:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    showAllContact();
                }else{
                    Toast.makeText(getApplicationContext(),"请先给予通讯录权限！",Toast.LENGTH_SHORT).show();
                }
                return;
            case 111:
                //判断用户是否同意授权
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    callPhone(willCallPhone);
                }else{
                    Toast.makeText(getApplicationContext(),"没有获取到电话权限！",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }




}
