package tw.org.iii.androidgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by iii on 2017/3/23.
 */
public class home extends Activity {

    /*************************************** START解析JSON ***************************************/
    private void 讀取Json記憶碎片() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String strURL = "http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/gamescheduleWebAPI.jsp?member_id="+sharedPreferences.getString("id","1");
        Request request = new Request.Builder().url(strURL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHTTP", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String strJson = response.body().string();
                Log.d("OKHTTP", strJson);
                parseJson記憶碎片(strJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String 記憶碎片數量 = memoryPirces.get(0).getTimeshard_num();
                        目前的記憶碎片數量.setText(記憶碎片數量);
                        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                        sharedPreferences.edit().putInt("count",Integer.parseInt(記憶碎片數量)).apply();
//                        Toast.makeText(home.this, 記憶碎片數量 + "---", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void parseJson記憶碎片(String strJson) {
        memoryPirces = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String member_id = obj.getString("member_id");
                String timeshard_num = obj.getString("timeshard_num");
                MemoryPieces memoryPiece = new MemoryPieces(member_id, timeshard_num);
                memoryPirces.add(memoryPiece);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void 讀取Json全部衣服() {
        String strURL = "http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/itemWebAPI.jsp";
        Request request = new Request.Builder().url(strURL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHTTP", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String strJson = response.body().string();
                Log.d("OKHTTP", strJson);
                parseJsonClothes(strJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("aa", "" + items.size());
                        size = items.size();
//                        String money_price = String.valueOf(items.get(10-1).getMoney_price());
//                        Toast.makeText(home.this, money_price + "", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void parseJsonClothes(String strJson) {

        items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            Log.d("AAAAA", "star11" + String.valueOf(jsonArray.length()));
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int item_id = Integer.parseInt(obj.getString("item_id"));
                String itemUrl = obj.getString("itemUrl");
                int money_price = Integer.parseInt(obj.getString("money_price"));
                item = new Items(item_id, itemUrl, money_price);
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void parseJson1(String s) {

        try {
            final JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("supplystation_name");
                String lat = obj.getString("supplystation_latitude");
                String lng = obj.getString("supplystation_longitude");
                int Coin = obj.getInt("coin");
                Log.d("CCCCC:",name+"/"+lat+"/"+lng+"/"+Coin);
                tw.org.iii.androidgame.Marker m = new tw.org.iii.androidgame.Marker(name, lat, lng, Coin);
                mar.add(m);

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayList<tw.org.iii.androidgame.Marker> m = mar;

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*************************************** END解析JSON ***************************************/

    String[] dialogues;
    int choose = 0;
    private View.OnClickListener btnShoppingMall_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(CDictionary.ShopCode, "可愛服裝限時優惠中～");
            Intent intent = new Intent(home.this, ShoppingMall.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, CDictionary.reqCodeShop);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (data.getExtras() == null)
            return;
        if (requestCode == 123) {
            String result = data.getExtras().getString("aa");
            int count = data.getIntExtra("count",0);
            TextView Count = (TextView) findViewById(R.id.Count);
            Count.setText(Integer.toString(count));
            iv1.setImageResource(getResources().getIdentifier(result, "drawable", getPackageName()));
            iv1.setTag(result);
        }
        if (requestCode == CDictionary.reqCodeShop) {
            String result = data.getExtras().getString(CDictionary.ShopCode);
            Toast.makeText(home.this, result, Toast.LENGTH_SHORT).show();
        }
        if(resultCode==0){
            int result2 = data.getExtras().getInt("count");
//            if(result2==0){
//                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
//                result2 = sharedPreferences.getInt("count",0);
//                TextView Count = (TextView) findViewById(R.id.Count);
//                Count.setText(result2);
//            }else {
//                TextView Count = (TextView) findViewById(R.id.Count);
//                Count.setText(Integer.toString(result2));
//            }
            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            p = sharedPreferences.getBoolean("p", false);
            if (MapsActivity.count == 5 || MapsActivity.count == 10 || MapsActivity.count == 15 || MapsActivity.count == 20 || MapsActivity.count == 25 || MapsActivity.count == 30 || MapsActivity.count == 35){
                if (p == false) {
                    sharedPreferences.edit().putBoolean("p",true).apply();
                    Intent intent = new Intent(this, polt.class);
                    startActivity(intent);
                }
            }
//            if (MapsActivity.count > 5 && MapsActivity.count < 10 || MapsActivity.count > 10 && MapsActivity.count < 15  || MapsActivity.count > 15 && MapsActivity.count < 20  || MapsActivity.count > 20 && MapsActivity.count < 25  || MapsActivity.count > 25 && MapsActivity.count < 30  || MapsActivity.count > 30 && MapsActivity.count < 35  || MapsActivity.count > 35) {
//                sharedPreferences.edit().putBoolean("p",false).apply();
//            }
            TextView Count = (TextView) findViewById(R.id.Count);
                Count.setText(Integer.toString(result2));

            if (sharedPreferences.getInt("count",0)>=5 && sharedPreferences.getInt("count",0)<10) {
                dialogues = new String[]{"終於想起一點回憶了...", "這邊的世界果然不是我原本居住的地方呢", "上次回憶中兩人的臉似曾相識 是我認識的人嗎?", "謝謝你帶我出去尋找記憶", "回憶中的房間是我的房間嗎?", "去晃晃吧?"};
            }
            if (sharedPreferences.getInt("count",0)>=10 && sharedPreferences.getInt("count",0)<15) {
                dialogues = new String[]{"記憶一點一點的找回來了", "記憶中的那兩個人是我的父母親吧", "出去走走尋找記憶吧！", "唔...總覺得在哪裡看過這種食物"};
            }
            if (sharedPreferences.getInt("count",0)>=15 && sharedPreferences.getInt("count",0)<20) {
                dialogues = new String[]{"跟我一起在車上的人不知道怎麼樣了呢", "上次回憶中的車速非常的快 一定是很要緊的事", "要不要去逛逛其他地方?", "記憶..記憶..去哪裡能找到記憶碎片?", "有沒有別件衣服能讓我替換呢?"};
            }
            if (sharedPreferences.getInt("count",0)>=20 && sharedPreferences.getInt("count",0)<25) {
                dialogues = new String[]{"黑煙... 閃光... 是煙火嗎", "壟罩著天空的黑煙是哪裡來的?", "同一件衣服穿的有點久了 想換一件！", "睡覺的時候夢到螢火蟲了呢"};
            }
            if (sharedPreferences.getInt("count",0)>=25 && sharedPreferences.getInt("count",0)<30) {
                dialogues = new String[]{"想要一件新衣服！", "要快點把記憶都回想起來！", "出門去晃晃如何?", "地震好可怕啊！"};
            }
            if (sharedPreferences.getInt("count",0)>=30 && sharedPreferences.getInt("count",0)<35) {
                dialogues = new String[]{"嗯..奇怪的裝置是做什麼用的?", "去尋找記憶碎片吧！", "為什麼父母要哭呢?", "出去走走吧！", "吶吶，買件新衣服給我穿吧！"};
            }
            if (sharedPreferences.getInt("count",0)>=35) {
                dialogues = new String[]{"原來的世界不知道怎麼樣了...", "...再也見不到父母親了", "出門去逛逛吧！", "只要看見那張照片就會想起之前的生活..."};
            }


        }
    }

    private View.OnClickListener btnMaps_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);
            if (mar.size() != 0) {
                Intent inte = new Intent(home.this, MapsActivity.class);
                startActivityForResult(inte, 0);
            }
        }
    };
    private View.OnClickListener btnchangeStyle_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);
            Bundle bund = new Bundle();
            bund.putString(CDictionary.StyleCode, iv1.getTag().toString());
            Intent inte = new Intent(home.this, changeStyle.class);
            inte.putExtras(bund);
            startActivityForResult(inte, 123);

        }
    };
    private View.OnClickListener btndialogue_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);
            imageView1.setImageResource(pic[q]);
            q++;
            if (q == pic.length){
                q = 0;
            }

            talkBack.setVisibility(View.VISIBLE);
            talkView.setVisibility(View.VISIBLE);
            choose = (int) (Math.random() * dialogues.length);
            talkView.setText(dialogues[choose]);
        }
    };
    private View.OnClickListener talkView_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);
            talkBack.setVisibility(View.INVISIBLE);
            talkView.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initialConponemt();
        final Request request = new Request.Builder()
                .url("http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/supplystationWebAPI.jsp")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d("OKOKOKOK", json);
                parseJson1(json);

            }
        });
        bm.musicCreate(this, R.raw.rains);
        bm.musicPlay();
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        if (sharedPreferences.getInt("count",0)<5) {
            dialogues = new String[]{"這裡是...哪裡?", "我的名字是? 想不起來...", "你是我的親人嗎?", "可以帶我去尋找父母嗎?", "你知道要怎麼找回記憶嗎?", "知道有關於我的事情嗎?", "這是我本來居住的世界嗎?", "還是什麼都想不起來...", "我為什麼會失去記憶呢?", "你是怎麼找到我的呢?"};
        }
        if (sharedPreferences.getInt("count",0)>=5 && sharedPreferences.getInt("count",0)<10) {
            dialogues = new String[]{"終於想起一點回憶了...", "這邊的世界果然不是我原本居住的地方呢", "上次回憶中兩人的臉似曾相識 是我認識的人嗎?", "謝謝你帶我出去尋找記憶", "回憶中的房間是我的房間嗎?", "去晃晃吧?"};
        }
        if (sharedPreferences.getInt("count",0)>=10 && sharedPreferences.getInt("count",0)<15) {
            dialogues = new String[]{"記憶一點一點的找回來了", "記憶中的那兩個人是我的父母親吧", "出去走走尋找記憶吧！", "唔...總覺得在哪裡看過這種食物"};
        }
        if (sharedPreferences.getInt("count",0)>=15 && sharedPreferences.getInt("count",0)<20) {
            dialogues = new String[]{"跟我一起在車上的人不知道怎麼樣了呢", "上次回憶中的車速非常的快 一定是很要緊的事", "要不要去逛逛其他地方?", "記憶..記憶..去哪裡能找到記憶碎片?", "有沒有別件衣服能讓我替換呢?"};
        }
        if (sharedPreferences.getInt("count",0)>=20 && sharedPreferences.getInt("count",0)<25) {
            dialogues = new String[]{"黑煙... 閃光... 是煙火嗎", "壟罩著天空的黑煙是哪裡來的?", "同一件衣服穿的有點久了 想換一件！", "睡覺的時候夢到螢火蟲了呢"};
        }
        if (sharedPreferences.getInt("count",0)>=25 && sharedPreferences.getInt("count",0)<30) {
            dialogues = new String[]{"想要一件新衣服！", "要快點把記憶都回想起來！", "出門去晃晃如何?", "地震好可怕啊！"};
        }
        if (sharedPreferences.getInt("count",0)>=30 && sharedPreferences.getInt("count",0)<35) {
            dialogues = new String[]{"嗯..奇怪的裝置是做什麼用的?", "去尋找記憶碎片吧！", "為什麼父母要哭呢?", "出去走走吧！", "吶吶，買件新衣服給我穿吧！"};
        }
        if (sharedPreferences.getInt("count",0)>=35) {
            dialogues = new String[]{"原來的世界不知道怎麼樣了...", "...再也見不到父母親了", "出門去逛逛吧！", "只要看見那張照片就會想起之前的生活..."};
        }
//        Intent intent = getIntent();
//        int count = intent.getIntExtra("count", 0);
//        TextView Count = (TextView) findViewById(R.id.Count);
//        Count.setText(count);
//        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
//        if (sharedPreferences.getInt("count", 0) != 0) {
//            Count.setText(sharedPreferences.getInt("count", 0));
//        }

        讀取Json全部衣服();
        讀取Json記憶碎片();
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        alertId = soundPool.load(this, R.raw.buttonpressed, 1);


        Log.d("OOOOOOO",sharedPreferences.getInt("coin",888)+"");
        if (sharedPreferences.getInt("count1",0) == 0) {
            sharedPreferences.edit().putInt("count1", 1).apply();
            sharedPreferences.edit().putInt("coin", 1000).apply();
        }

        Log.d("home","2");

        Log.d("ID",memberId+"");
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

    private void initialConponemt() {
        目前的記憶碎片數量 = (TextView) findViewById(R.id.Count);

        imageView1 = (ImageView) findViewById(R.id.face);
        btnMaps = (Button) findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(btnMaps_Click);
        btnchangeStyle = (Button) findViewById(R.id.btnChangeStyle);
        btnchangeStyle.setOnClickListener(btnchangeStyle_Click);
        btndialogue = (Button) findViewById(R.id.btndialogue);
        btndialogue.setOnClickListener(btndialogue_Click);
        talkView = (TextView) findViewById(R.id.talkView);
        talkView.setOnClickListener(talkView_Click);
        iv1 = (ImageView) findViewById(R.id.imageView);
        talkBack = (ImageView) findViewById(R.id.talkBack);
        talkBack.setOnClickListener(talkView_Click);
        btnShoppingMall = (Button) findViewById(R.id.btnShoppingMall);
        btnShoppingMall.setOnClickListener(btnShoppingMall_Click);
    }


    protected static String memberId;
    protected static int size;

    private ArrayList<MemoryPieces> memoryPirces;
    private Items item;
    protected static ArrayList<Items> items;

    private OkHttpClient client = new OkHttpClient();
    private Button btnShoppingMall;

    private SoundPool soundPool;
    private int alertId;
    protected static ArrayList<Marker> mar = new ArrayList<>();
    int[] pic = {R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.j,R.drawable.h,R.drawable.i};
    int q = 0;
    BgmManager bm = new BgmManager();

    boolean p = false;
    protected static TextView 目前的記憶碎片數量;
    Button btnMaps;
    Button btndialogue;
    Button btnchangeStyle;
    ImageView imageView1;
    ImageView iv1;
    TextView talkView;
    ImageView talkBack;
}
