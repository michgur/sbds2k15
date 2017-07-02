package com.cyhim.sbds2k15;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class SBDS
{
    private static final ColorDrawable BACKGROUND = new ColorDrawable(-4146234);
    private static final int BUTTON = R.drawable.sbdbutton;
    private static final int BUTTON_SELECTED = R.drawable.sbdbutton_selected;
    private static final int GLOW_RES = R.drawable.glow;
    
    private GameActivity activity;
    private int attempts = 0;
    private TextView attemptsView;
    private LoveBar loveBar;
    private TextView mk;
    private final Runnable onSbEnd = new Runnable()
    {
        public void run() {
            if (won) gameOver();

            List<SBDOption> next = SBDOption.getRandomOptions(options.length);
            for (int i = 0; i < options.length; i++) {
                OptionButton o = options[i];
                o.getFrame().setBackground(SBDS.BACKGROUND);
                o.setOption(next.get(i));
                o.update();
                o.getButton().setEnabled(true);
                o.getButton().setBackgroundResource(2130837601);
            }
        }
    };

    private OptionButton[] options = new OptionButton[3];
    private Resources resources;
    private TypeWriter sb;
    private ImageView sbImage;
    private String[] sbNames = {
            "Spongebob",
            "Spobnoj",
            "Snobnob",
            "Bobsponge",
            "Sphopho",
            "Spongador",
            "Sbolbol",
            "Pospos",
            "Bapi Boopie Doo",
            "Spangbab",
            "Speepee",
            "Patrick",
            "Partick"
    };
    private Button start;
    private int time = 0;
    private Runnable timeRunnable = new Runnable() {
        public void run() {
            timer.setText(String.format("%02d:%02d", ++time / 60, time % 60));
            timerHandler.postDelayed(this, 1000);
        }
    };
    private TextView timer;
    private Handler timerHandler;
    private boolean won = false;

    public SBDS(View view, Resources resources, final GameActivity activity) {
        this.activity = activity;
        this.resources = resources;

        sbImage = ((ImageView) view.findViewById(R.id.sbImage));
        start = ((Button) view.findViewById(R.id.starting));
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timerHandler.postDelayed(new Runnable() {
                    public void run() {
                        start.setVisibility(View.INVISIBLE);
                        timerHandler.postDelayed(timeRunnable, 1000);
                    }
                }, 500);
            }
        });

        mk = (TextView) view.findViewById(R.id.mkText);
        sb = (TypeWriter) view.findViewById(R.id.sbText);
        sb.setSound(R.raw.eeh);
        if (new Random().nextInt(4) == 0) sb.setText(getSBName());
        if ((sb.getText().equals("Patrick")) || (sb.getText().equals("Partick")))
            sbImage.setImageResource(R.drawable.pt);

        sb.setOnFinishCallback(onSbEnd);
        SBDOption.setOptions(R.xml.options, resources);
        options[0] = new OptionButton(view, R.id.b0, R.id.f0);
        options[1] = new OptionButton(view, R.id.b1, R.id.f1);
        options[2] = new OptionButton(view, R.id.b2, R.id.f2);
        onSbEnd.run();

        for (OptionButton o : options) bindStuff(o);

        loveBar = new LoveBar(view, R.id.love_container);
        attemptsView = (TextView) view.findViewById(R.id.attepmts);
        attemptsView.setText(resources.getString(R.string.attempts, 0));
        timer = (TextView) view.findViewById(R.id.timer);
        timer.setText("00:00");
        timerHandler = new Handler();
    }

    private void bindStuff(final OptionButton o) {
        o.getButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speak(o.getOption().getMk(), o.getOption().getSb());

                loveBar.add(o.getOption().getValue());
                if (loveBar.getCount() == 10) won = true;

                attemptsView.setText(resources.getString(R.string.attempts, ++attempts));

                o.getFrame().setBackgroundResource(GLOW_RES);
                o.getButton().setBackgroundResource(BUTTON_SELECTED);
                for (OptionButton o : options) o.getButton().setEnabled(false);
            }
        });
    }

    private void gameOver() {
        pause();
        int score = (int)(1.0 / (time * 123457.126742) * 1.1349124539821739E14D), highscore;
        SharedPreferences prefs = activity.getSharedPreferences("SBDSPrefs", 0);
        if ((highscore = prefs.getInt("highscore", 0)) < score)
            prefs.edit().putInt("highscore", score).commit();

        Builder builder = new Builder(activity);
        builder.setPositiveButton("OK", new OnClickListener() {  // BEHOLD THE MAGNIFICENT DIALOG TREE
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                Builder builder = new Builder(activity);
                builder.setPositiveButton("Yes", new OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        restart();
                        activity.hideSystemUI();
                    }
                });
                builder.setNegativeButton("No", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Builder builder = new Builder(activity);
                        builder.setPositiveButton("OK", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (Build.VERSION.SDK_INT >= 21) activity.finishAndRemoveTask();
                                else activity.finish();
                            }
                        });
                        builder.setMessage("You Suck.");
                        builder.create().show();
                    }
                });
                builder.setMessage("Would You Like To Replay?");
                builder.create().show();
            }
        });
        builder.setMessage(this.resources.getString(R.string.victory, score, highscore));
        builder.create().show();
        activity.hideSystemUI();
    }

    private String getSBName() { return sbNames[new Random().nextInt(sbNames.length)]; }

    private void restart() {
        pause();
        time = 0;
        attempts = 0;
        loveBar.add(-10);
        won = false;

        sb.setText(getSBName());
        if ((sb.getText().equals("Patrick")) || (sb.getText().equals("Partick")))
            sbImage.setImageResource(R.drawable.pt);

        mk.setText("U");
        attemptsView.setText("Attempts: 0");
        timer.setText("00:00");
        start.setVisibility(View.VISIBLE);
        onSbEnd.run();
        sbImage.setImageResource(R.drawable.sb2);
    }

    private void speak(String mkString, String sbString) {
        mk.setText(mkString);
        sb.animateText(sbString);
    }

    public void pause() { timerHandler.removeCallbacks(timeRunnable); }

    public void resume() {
        if ((start.getVisibility() == View.INVISIBLE) && !won) {
            pause();
            timerHandler.postDelayed(timeRunnable, 1000);
        }
    }

    private static class OptionButton
    {
        private Button button;
        private FrameLayout frame;
        private SBDOption option;

        public OptionButton(View view, @IdRes int button, @IdRes int frame) {
            this.button = (Button) view.findViewById(button);
            this.frame = (FrameLayout) view.findViewById(frame);
        }

        public Button getButton() { return button; }
        public FrameLayout getFrame() { return frame; }
        public SBDOption getOption() { return option; }

        public void setOption(SBDOption option) { this.option = option; }

        public void update() { button.setText(option.getMk()); }
    }
}
