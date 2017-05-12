package tw.org.iii.androidgame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import static tw.org.iii.androidgame.home.memberId;

/**
 * Created by iii on 2017/3/23.
 */

public class OpenVideo extends Activity {

    private MediaPlayer.OnCompletionListener onMovieCompleteHandler = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Intent inte = new Intent(OpenVideo.this, MainActivity.class);
            startActivity(inte);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openvideo);
            /*getActionBar().hide();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);*/

        vidView = (VideoView) findViewById(R.id.video1);
        TextView txt = (TextView) findViewById(R.id.txt);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(OpenVideo.this, MainActivity.class);
                startActivity(inte);
                finish();
            }

        });

        if (memberId != null) {
            startActivity(new Intent(OpenVideo.this, MainActivity.class));
            finish();
        } else {
            vidControl = new MediaController(this);
            vidControl.setVisibility(View.INVISIBLE);
            vidView.setMediaController(vidControl);
            vidView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.op169));
            vidView.start();
            vidView.setOnCompletionListener(onMovieCompleteHandler);
        }
    }

    private VideoView vidView;
    private MediaController vidControl;
}
