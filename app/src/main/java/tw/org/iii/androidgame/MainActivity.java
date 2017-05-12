package tw.org.iii.androidgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static tw.org.iii.androidgame.home.目前的記憶碎片數量;

public class MainActivity extends Activity {



    private void is登入() {
        SharedPreferences logined = getSharedPreferences(CDictionary.logined, 0);
        String account = logined.getString(CDictionary.Account, "尚未註冊喔");
        if (account.contains("尚未註冊喔")) {
            startActivity(new Intent(MainActivity.this, MemberLogin.class));
        } else {
            startActivity(new Intent(MainActivity.this, home.class));
        }
    }

    private View.OnClickListener imageView_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            is登入();
            finish();
        }
    };

    private View.OnClickListener linearLayout_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            is登入();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initailComponent();
    }

    private void initailComponent() {
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(imageView_Click);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(linearLayout_Click);
    }

    private ImageView imageView;
    private LinearLayout linearLayout;
}
