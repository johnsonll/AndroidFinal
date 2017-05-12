package tw.org.iii.androidgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by iii on 2017/3/23.
 */

// 使用 jsoup 對 HTML 文檔進行解析和操作
// https://www.ibm.com/developerworks/cn/java/j-lo-jsouphtml/

public class changeStyle extends Activity {

    private void 讀取Json換裝() {
        SharedPreferences logined = getSharedPreferences(CDictionary.logined, 0);
        String memberId = logined.getString(CDictionary.MemberId, "沒資料");
        String strURL = "http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/characterWebAPI.jsp?member_id=" + memberId;
        Request request = new Request.Builder().url(strURL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHTTP", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String strJson = response.body().string();
                Log.d("OKHTTP", strJson);
                parseJson(strJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(changeStyle.this, styles.size() + "", Toast.LENGTH_LONG).show();

                        for (int i = 0; i < styles.size(); i++) {
                            lista.add(styles.get(i).getItem_id());
                        }
//                        Toast.makeText(changeStyle.this,lista.size()+"",Toast.LENGTH_LONG).show();
                        InitialImageView(lista.size());
                        Log.d("aa", "" + styles.size());
                    }
                });

            }
        });
    }

    private void parseJson(String strJson) {
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            Log.d("AAAAA", "star11" + String.valueOf(jsonArray.length()));
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int member_id = obj.getInt("member_id");
                int item_id = obj.getInt("item_id");
                style = new Style(member_id, item_id);
                styles.add(style);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener btnChangeCloth_CLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (id == null)
                return;
            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            int count = sharedPreferences.getInt("count",0);
            Intent itnt = new Intent();
            Bundle bund = new Bundle();
            bund.putString("aa", id);
            bund.putInt("count",count);
            itnt.putExtras(bund);
            setResult(0, itnt);
            finish();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changestyle);
        bm.musicCreate(this, R.raw.clearsky);
        bm.musicPlay();
        initialComponent();
        Intent intent = getIntent();
        Bundle bundled = intent.getExtras();
        String style = bundled.getString(CDictionary.StyleCode);
        model.setImageResource(getResources().getIdentifier(style, "drawable", getPackageName()));

        讀取Json換裝();

    }

    private void InitialImageView(int 圖片數量) {
        int imgCount = 圖片數量 / 4;
//        String aa = "";
////        aa += "modeling" + (lista.get(0) + "\n");
//        for (int i = 0; i < imgCount; i++) {
//            for (int j = 0; j < 4; j++) {
//                aa += "modeling" + (j * 4 + i) + "\n";
//            }
//        }
//        Toast.makeText(changeStyle.this, aa, Toast.LENGTH_LONG).show();

        int width = 275;
        int height = 400;
        LinearLayout linearLayoutView = (LinearLayout) findViewById(R.id.linearLayout);

        for (int j = 0; j < imgCount; j++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundResource(R.drawable.border1);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

            for (int i = 0; i < 4; i++) {
//                aa += lista.get(j + i * 圖片數量 / 4+1) + "\n";
                imageView = new ImageView(this);
//                imageView.setImageResource(getResources().getIdentifier("modeling" + (j * 4 + i), "drawable", getPackageName()));
                imageView.setImageResource(getResources().getIdentifier("modeling" + lista.get((j * 4 + i)), "drawable", getPackageName()));
                imageView.setBackgroundResource(R.drawable.border1);
//                imageView.setTag("modeling" + (lista.get(j + i * 圖片數量 / 4 + 1)));
                imageView.setTag("modeling" + lista.get((j * 4 + i)));
                imageView.setLayoutParams(params);
                imageView.setOnClickListener(imageView_Click);
                linearLayout.addView(imageView);
                list.add(imageView);
            }
            linearLayoutView.addView(linearLayout);
        }
//        Toast.makeText(changeStyle.this, aa, Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener imageView_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clear();
            id = v.getTag().toString();
            String style = v.getTag().toString();
            model.setImageResource(getResources().getIdentifier(style, "drawable", getPackageName()));
            v.setBackgroundResource(R.drawable.border);
        }
    };

    private void clear() {
        for (ImageView iv : list) {
            iv.setBackgroundResource(R.drawable.border1);
        }
    }

    private void initialComponent() {
        btnChangeCloth = (Button) findViewById(R.id.btnChangeCloth);
        btnChangeCloth.setOnClickListener(btnChangeCloth_CLick);
        model = (ImageView) findViewById(R.id.model);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bm.musicStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bm.musicPlay();
    }

    private ArrayList<Integer> lista = new ArrayList<>();
    private Style style;
    private ArrayList<Style> styles = new ArrayList<>();

    private LinkedList<ImageView> list = new LinkedList<>();
    private OkHttpClient client = new OkHttpClient();

    BgmManager bm = new BgmManager();
    private ImageView imageView;
    private Button btnChangeCloth;
    private String id;
    private ImageView model;

}
