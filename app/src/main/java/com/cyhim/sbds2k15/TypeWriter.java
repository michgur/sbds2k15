package com.cyhim.sbds2k15;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.RawRes;
import android.util.AttributeSet;

public class TypeWriter extends android.support.v7.widget.AppCompatTextView
{
    private Runnable characterAdder = new Runnable() {
        public void run() {
            setText(text.subSequence(0, index));
            if (++index > text.length()) {
                if (onFinish != null) handler.postDelayed(onFinish, delay);
                return;
            }
            if (!Character.isAlphabetic(text.charAt(index - 1))) {
                handler.postDelayed(characterAdder, delay / 2);
                return;
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getContext(), soundId);
                mediaPlayer.start();
            }
            handler.postDelayed(characterAdder, delay);
        }
    };

    private int delay = 150;
    private Handler handler = new Handler();
    private int index;
    private MediaPlayer mediaPlayer;
    private Runnable onFinish;
    private int soundId;
    private CharSequence text;

    public TypeWriter(Context context) { super(context); }
    public TypeWriter(Context context, AttributeSet attributes) { super(context, attributes); }

    public void animateText(CharSequence text) {
        this.text = text;
        index = 0;
        setText("");
        handler.removeCallbacks(characterAdder);
        handler.postDelayed(characterAdder, delay);
    }

    public boolean isDone() { return index == text.length(); }

    public void setCharacterDelay(int delay) { this.delay = delay; }

    public void setOnFinishCallback(Runnable onFinish) { this.onFinish = onFinish; }

    public void setSound(@RawRes int resid) {
        if ((mediaPlayer != null) && (resid != soundId)) mediaPlayer.release();

        soundId = resid;
        mediaPlayer = MediaPlayer.create(getContext(), soundId);
    }
}
