package tw.org.iii.androidgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class polt extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polt);
        imageView1 = (ImageView) findViewById(R.id.img);
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        switch (sharedPreferences.getInt("count",0)){
            case 5 :
                imageView1.setImageResource(plot[0]);
                break;
            case 10 :
                imageView1.setImageResource(plot[1]);
                break;
            case 15 :
                imageView1.setImageResource(plot[2]);
                break;
            case 20 :
                imageView1.setImageResource(plot[3]);
                break;
            case 25 :
                imageView1.setImageResource(plot[4]);
                break;
            case 30 :
                imageView1.setImageResource(plot[5]);
                break;
            case 35 :
                imageView1.setImageResource(plot[6]);
                break;
        }

        if(sharedPreferences.getInt("count",0) == 5)
        imageView1.setImageResource(plot[0]);
    }
    public void clk (View v){
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        if ( sharedPreferences.getInt("count",0) != 35){
//            Intent intent = new Intent(this, home.class);
//            startActivity(intent);
            finish();
        }else {
            q += 1;
            if(q == 7 || q == 8){
//                finish();
                imageView1.setImageResource(plot[q]);
            }else{
//                Intent intent = new Intent(this, home.class);
//                startActivity(intent);
                finish();
            }
        }

    }
    int plot [] = {R.drawable.story1,R.drawable.story2,R.drawable.story3,R.drawable.story4,R.drawable.story5,R.drawable.story6,R.drawable.story71,R.drawable.story72,R.drawable.family};
    private int q = 6;
    ImageView imageView1;
}
