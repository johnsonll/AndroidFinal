package tw.org.iii.androidgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static tw.org.iii.androidgame.home.items;
import static tw.org.iii.androidgame.home.size;

public class ShoppingMall extends Activity {


    ;

    private void 讀取Json() {
        SharedPreferences sharedPreferences = getSharedPreferences(CDictionary.logined, 0);
        會員ID = sharedPreferences.getString(CDictionary.MemberId, "1");
        String strURL = "http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/characterWebAPI.jsp?member_id=" + 會員ID;
//        Toast.makeText(ShoppingMall.this, 會員ID, Toast.LENGTH_LONG).show();
        final Request request = new Request.Builder().url(strURL).build();
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
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void run() {
//                        Toast.makeText(ShoppingMall.this,items.size()+"",Toast.LENGTH_LONG).show();
                        尚未購買的衣服(size);
                        InitialImageView(未購買衣服list.size());
                    }
                });

            }
        });
    }

    private void 尚未購買的衣服(int 圖片數量) {
        for (int i = 1; i <= 圖片數量; i++) {
            未購買的衣服set.add(i);
        }

        //剔除已購買的衣服
        for (int i = 0; i < styles.size(); i++) {
            未購買的衣服set.remove(styles.get(i).getItem_id());
        }
        Iterator<Integer> it = 未購買的衣服set.iterator();
        while (it.hasNext()) {
            未購買衣服list.add(it.next());
        }

//        String aa = "";
//        for (Integer i : 未購買衣服list) {
//            aa += i + ",";
//        }
//        Toast.makeText(ShoppingMall.this, aa, Toast.LENGTH_LONG).show();
//        Toast.makeText(ShoppingMall.this, 未購買衣服list.size()+"", Toast.LENGTH_LONG).show();
    }


    private void parseJson(String strJson) {
        styles = new ArrayList<>();
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

    private View.OnClickListener imageView_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clear();
            id = v.getTag().toString();
            v.setBackgroundResource(R.drawable.border);
        }
    };

    private void clear() {
        for (ImageView iv : 衣服s) {
            iv.setBackgroundResource(R.drawable.border1);
        }
    }

    private View.OnClickListener btn購買_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            SharedPreferences logined = getSharedPreferences(CDictionary.logined, 0);
//            String account = logined.getString(CDictionary.Account, "尚未註冊喔");
//            if (account.contains("尚未註冊喔")) {
//                startActivity(new Intent(ShoppingMall.this, MemberLogin.class));
//            } else {
            try {
                購買上傳資料庫();
                Toast.makeText(ShoppingMall.this, "已購買可愛的" + id + "服裝", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
//                e.printStackTrace();
                Toast.makeText(ShoppingMall.this, "忘記選擇衣服囉", Toast.LENGTH_SHORT);
                return;
            }
            Intent intent = getIntent().putExtra(CDictionary.ShopCode, "已購買可愛的" + id + "服裝");
            setResult(CDictionary.rsCode, intent);
//            }
//            finish();
        }

        private void 購買上傳資料庫() throws IOException {
            String total;
            String clothesId;
            if (id.length() != 10) {
                clothesId = id.substring(id.length() - 1, id.length());
            } else {
                clothesId = id.substring(id.length() - 2, id.length());
            }

            SharedPreferences sharedPreferences = getSharedPreferences(CDictionary.logined, 0);
            String memberId = sharedPreferences.getString(CDictionary.MemberId, "null");

//            Toast.makeText(ShoppingMall.this, clothesId + "----", Toast.LENGTH_SHORT).show();
            total = String.valueOf(items.get(Integer.parseInt(clothesId)).getMoney_price());

            SharedPreferences sharedPreference = getSharedPreferences("data", MODE_PRIVATE);

//            購買扣款
//            if (count < 1) {
//                int 初始金幣值 = sharedPreference.getInt("coin",0);
            int 衣服單價 = Integer.parseInt(total);
////            Toast.makeText(ShoppingMall.this, 衣服單價 + "----", Toast.LENGTH_SHORT).show();
            金幣餘額 = sharedPreference.getInt("coin",0) - 衣服單價;
            sharedPreference.edit().putInt("coin",金幣餘額).apply();
            金幣.setText("\uD83D\uDCB0" + sharedPreference.getInt("coin",666));
//                count++;
//            } else {
//                int 衣服單價 = Integer.parseInt(total);
////            Toast.makeText(ShoppingMall.this, 衣服單價 + "----", Toast.LENGTH_SHORT).show();
//                金幣餘額 -= 衣服單價;
//                金幣.setText("\uD83D\uDCB0" + 金幣餘額);
//            }

//            int money_price = items.get().getMoney_price();

            String str購買URL = "http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/shoppingrecordWebAPI.jsp?";
            String str購買Para = "member_id=" + memberId;
            str購買Para += "&item_id=" + clothesId;
            str購買Para += "&total=" + total;

//            Toast.makeText(ShoppingMall.this, str購買URL + str購買Para, Toast.LENGTH_LONG).show();
            //下面打開就是購買傳送資料庫
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(str購買URL + str購買Para).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(ShoppingMall.this, "連線失敗～", Toast.LENGTH_SHORT);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        }
    };

//    private View.OnClickListener btn取消_Click = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            ShoppingMall.this.finish();
//        }
//    };

    private void 會員金幣() {
//        SharedPreferences sharedPreferences = getSharedPreferences(CDictionary.Gold, 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(CDictionary.金幣,2000).commit();

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);


        金幣.setText("\uD83D\uDCB0" + sharedPreferences.getInt("coin",0));

//        if (count1 != 1) {
//            金幣.setText("\uD83D\uDCB0" + 2000);
//            Toast.makeText(ShoppingMall.this, ""+count1, Toast.LENGTH_LONG).show();
//            count1++;
//
//        } else {
//            金幣.setText("\uD83D\uDCB0" + 金幣餘額);
//            Toast.makeText(ShoppingMall.this, "\uD83D\uDCB0" + 金幣餘額, Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_mall);
        bm.musicCreate(this, R.raw.walkin);
        bm.musicPlay();

        讀取Json();
        InitialComponent();
        會員金幣();

//        Toast.makeText(ShoppingMall.this, memberId, Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        String data = intent.getExtras().getString(CDictionary.ShopCode);
        Toast.makeText(ShoppingMall.this, data, Toast.LENGTH_LONG).show();
    }

    private void InitialComponent() {
        btn購買 = (Button) findViewById(R.id.btn購買);
        btn購買.setOnClickListener(btn購買_Click);
        金幣 = (TextView) findViewById(R.id.金幣);
//        btn取消 = (Button) findViewById(R.id.btn取消);
//        btn取消.setOnClickListener(btn取消_Click);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void InitialImageView(int 圖片數量) {
        int i1 = 圖片數量 / 4;
//        String aa = "";
//        for (int i = 0; i < i1 ; i++) {
//            for (int j = 0; j < 4; j++) {
//                aa += (未購買衣服list.get(j*4+i))  + ",";
//            }
//        }
//        Toast.makeText(ShoppingMall.this,aa, Toast.LENGTH_LONG).show();

        int width = 275;
        int height = 400;

        LinearLayout linearLayoutView = (LinearLayout) findViewById(R.id.linearLayout);
        for (int i = 0; i < i1; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundResource(R.drawable.border1);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            for (int j = 0; j < 4; j++) {
//                if (i + j * 4 <= 圖片數量) {
                Integer integer = 未購買衣服list.get(j + i * 4);
                if (integer < 1 || integer >= 40) {
                    return;
                } else {

////                    Toast.makeText(ShoppingMall.this, items.size() + "", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(ShoppingMall.this, items.get(integer).getMoney_price() + "", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(ShoppingMall.this, integer + "", Toast.LENGTH_SHORT).show();

                    LinearLayout linearLayoutInner = new LinearLayout(this);
                    linearLayoutInner.setOrientation(LinearLayout.VERTICAL);
                    linearLayoutInner.setBackgroundResource(R.drawable.border1);
                    LinearLayout.LayoutParams paramsInner = new LinearLayout.LayoutParams(width, 80);

                    imageView = new ImageView(this);
                    imageView.setImageResource(getResources().getIdentifier("modeling" + integer, "drawable", getPackageName()));
                    imageView.setBackgroundResource(R.drawable.border1);
                    imageView.setTag("modeling" + integer);
                    imageView.setLayoutParams(params);
                    imageView.setOnClickListener(imageView_Click);
                    linearLayoutInner.addView(imageView);

//                    String 衣服價格 = "\uD83D\uDCB2" + String.valueOf(items.get(未購買衣服list.get(j + i * 4)).getMoney_price());
                    String 衣服價格 = "\uD83D\uDCB2" + items.get(integer).getMoney_price();
                    TextView textView = new TextView(this);
                    textView.setText(衣服價格);
                    textView.setTextSize(22);
                    textView.setTextColor(Color.argb(100, 77, 0, 0));
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setLayoutParams(paramsInner);
                    linearLayoutInner.addView(textView);

                    linearLayout.addView(linearLayoutInner);
                    衣服s.add(imageView);
                }


//                }
            }
            linearLayoutView.addView(linearLayout);
        }
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


    private ArrayList<Integer> 未購買衣服list = new ArrayList<>();
    private TreeSet<Integer> 未購買的衣服set = new TreeSet<>();

    private Style style;
    private ArrayList<Style> styles;
    private OkHttpClient client = new OkHttpClient();

    private int 金幣餘額;
    int 金幣值;
    private String 會員ID;
    private int 目前金幣數;
    public String id;
    private ImageView imageView;
    private LinkedList<ImageView> 衣服s = new LinkedList<>();

    private TextView 金幣;
    BgmManager bm = new BgmManager();
    private Button btn取消;
    private Button btn購買;
}
