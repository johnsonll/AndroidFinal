package tw.org.iii.androidgame;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static tw.org.iii.androidgame.home.memberId;

public class MemberLogin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);
        initialcomponent();

        //-----------取得register Layout reference----------
        inflater = LayoutInflater.from(MemberLogin.this);

    }

    private OkHttpClient client = new OkHttpClient();
    String json;
    LayoutInflater inflater;
    View registerView;

    private View.OnClickListener btn登入_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Request request = new Request.Builder()
                    .url("http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/memberWebAPI.jsp?emailLogin=" + ET帳號.getText().toString() + "&passwordLogin=" + ET密碼.getText().toString())
//                    .url("http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/memberWebAPI.jsp?emailLogin=aaa@gmail.com&passwordLogin=bbb")
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    json = response.body().string();
                    Log.d("OKHTTP", json);
                    try {
                        parseGson(json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lbltest.setText("login success,你好" + ET帳號.getText().toString() + "!");
                                SharedPreferences logined = getSharedPreferences(CDictionary.logined, 0);
                                SharedPreferences.Editor editor = logined.edit();
                                editor.putString(CDictionary.Account, ET帳號.getText().toString()).commit();
                                editor.putString(CDictionary.Password, ET密碼.getText().toString()).commit();
                                editor.putString(CDictionary.MemberId, String.valueOf(list.get(0).getMember_id())).commit();
                                memberId = String.valueOf(list.get(0).getMember_id());

                                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                sharedPreferences.edit().putString("id",memberId).apply();
                                Log.d("memberId",memberId+"");
                                startActivity(new Intent(MemberLogin.this, home.class));
                                finish();

//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        /*** 修正位置****/
//                                        memberId = String.valueOf(list.get(0).getMember_id());
//                                    }
//                                });
                            }
                        });
                    } catch (IllegalStateException | JsonSyntaxException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lbltest.setText("輸入帳號密碼錯誤");
                            }
                        });
                    }
                }
            });

        }
    };

    private void parseGson(String strJson) {
        Gson gson = new Gson();
        list = gson.fromJson(strJson, new TypeToken<ArrayList<Member>>() {
        }.getType());
        Log.d("JSON", list.size() + "/" + list.get(0).getEmail());
    }

    private DialogInterface.OnClickListener btnOK_click = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (!registerEmail.getText().toString().equals("") && !registerPwd.getText().toString().equals("") && !registerPwdCheck.getText().toString().equals("") && !registerName.getText().toString().equals("") && !registerTel.getText().toString().equals("")) {
                Toast.makeText(MemberLogin.this, "有資料", Toast.LENGTH_LONG).show();
                if (registerPwd.getText().toString().equals(registerPwdCheck.getText().toString())) {

                    Request request = new Request.Builder()
                            .url("http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/memberWebAPI.jsp?" +
                                    "userEmail=" + registerEmail.getText().toString() +
                                    "&userPwd=" + registerPwd.getText().toString() +
                                    "&userName=" + registerName.getText().toString() +
                                    "&userPhone=" + registerTel.getText().toString() +
                                    "&userPasswordCheck=" + registerPwdCheck.getText().toString())
                            .build();
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MemberLogin.this, "註冊成功", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } catch (Exception e) {
                                Toast.makeText(MemberLogin.this, "請輸入正確資料", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MemberLogin.this, "密碼與密碼確認不符", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MemberLogin.this, "請輸入資料", Toast.LENGTH_LONG).show();
            }
        }
    };
    private View.OnClickListener btn註冊_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerView = inflater.inflate(R.layout.activity_member, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(MemberLogin.this);
            registerEmail = (EditText) registerView.findViewById(R.id.registerEmail);
            registerPwd = (EditText) registerView.findViewById(R.id.registerPwd);
            registerName = (EditText) registerView.findViewById(R.id.registerName);
            registerTel = (EditText) registerView.findViewById(R.id.registerTel);
            registerPwdCheck = (EditText) registerView.findViewById(R.id.registerPwdCheck);
            builder.setTitle("                 會員註冊");


            builder.setView(registerView);


            builder.setPositiveButton("OK", btnOK_click);
            Dialog message = builder.create();
            message.show();

        }
    };


    private void initialcomponent() {


        ET帳號 = (EditText) findViewById(R.id.userEmailLogin);
        ET密碼 = (EditText) findViewById(R.id.userPwdLogin);
        btn登入 = (Button) findViewById(R.id.btn登入);
        btn登入.setOnClickListener(btn登入_Click);
        btn註冊 = (Button) findViewById(R.id.btn註冊);
        btn註冊.setOnClickListener(btn註冊_Click);

        lbltest = (TextView) findViewById(R.id.lbltest);
    }

    protected static ArrayList<Member> list;

    EditText registerEmail;
    EditText registerPwd;
    EditText registerName;
    EditText registerTel;
    EditText registerPwdCheck;

    EditText ET帳號;
    EditText ET密碼;
    Button btn登入;
    Button btn註冊;
    TextView lbltest;
}


