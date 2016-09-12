package aohan.zl.echo.echo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class MenuActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemRefresh:
                Toast.makeText(getApplicationContext(),"刷新",Toast.LENGTH_SHORT).show();
                break;
            case R.id.itemReset:
                Toast.makeText(getApplicationContext(),"重置",Toast.LENGTH_SHORT).show();
                break;
            case R.id.itemShare:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra("data","测试数据");
                startActivity(Intent.createChooser(intent,"选择"));
                break;
            case R.id.itemToSelectFileActivity:
                startActivity(new Intent(getApplicationContext(),FileSelectActivity.class));
                break;
        }
        return  true;
    }
}
