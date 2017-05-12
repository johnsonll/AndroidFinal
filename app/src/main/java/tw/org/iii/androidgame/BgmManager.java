package tw.org.iii.androidgame;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by iii on 2017/3/30.
 */

public class BgmManager extends Activity {
    MediaPlayer mp;
    public void musicCreate(Context a, int b) {
        mp = new MediaPlayer().create(a, b);
    }
    public void musicPlay() {
        mp.start();
    }
    public void musicStop() {
        mp.pause();
    }
}
