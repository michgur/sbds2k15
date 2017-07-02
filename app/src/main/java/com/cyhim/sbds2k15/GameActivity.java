package com.cyhim.sbds2k15;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GameActivity extends AppCompatActivity
{
    private View view;
    private SBDS game;

    @SuppressLint("InlinedApi")
    public void hideSystemUI() { view.setSystemUiVisibility(4871); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game);

        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.hide();

        view = findViewById(R.id.screen);
        hideSystemUI();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        game = new SBDS(view, getResources(), this);
        SharedPreferences prefs = getSharedPreferences("SBDSPrefs", 0);
        if (prefs.getBoolean("showIntro", true)) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
            localBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    hideSystemUI();
                }
            });
            localBuilder.setMessage("Hello, and Welcome to the Spongebob Dating Simulator 2015, REMASTERED for Your Smartphone!\nIn This Game, You Take the Role of Mr. Krabs, the Owner of the Krusty Krab. You're Dating Spongebob,Your Worker and Love Interest, in an Attempt to Make Him Fall in Love With You.\nTry to Guess What You Should Say in Order to Progress The Love, in The Fastest Way Possible.\nGood Luck, and Have Fun!");
            localBuilder.create().show();
            prefs.edit().putBoolean("showIntro", false).apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideSystemUI();
        game.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }
}
