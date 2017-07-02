package com.cyhim.sbds2k15;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoveBar
{
    private TextView[] cells = new TextView[10];
    private LinearLayout container;
    private int count = 0;

    public LoveBar(View view, @IdRes int resid) {
        container = ((LinearLayout)view.findViewById(resid));
        cells[0] = (TextView) container.findViewById(R.id.heart0);
        cells[1] = (TextView) container.findViewById(R.id.heart1);
        cells[2] = (TextView) container.findViewById(R.id.heart2);
        cells[3] = (TextView) container.findViewById(R.id.heart3);
        cells[4] = (TextView) container.findViewById(R.id.heart4);
        cells[5] = (TextView) container.findViewById(R.id.heart5);
        cells[6] = (TextView) container.findViewById(R.id.heart6);
        cells[7] = (TextView) container.findViewById(R.id.heart7);
        cells[8] = (TextView) container.findViewById(R.id.heart8);
        cells[9] = (TextView) container.findViewById(R.id.heart9);
    }

    private void update() {
        for (int i = 0; i < count; i++) cells[i].setBackgroundColor(0xFF12FF45);
        for (int i = count; i < cells.length; i++) cells[i].setBackgroundColor(0xFFB30006);
    }

    public void add(int n) {
        if (n == 0) return;

        count = Math.min(Math.max(count + n, 0), 10);
        update();
    }

    public int getCount() { return this.count; }
}
