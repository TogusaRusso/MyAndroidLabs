package com.example.togusa.modernartui;

import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String URL_WWW_MOMA_ORG = "http://www.moma.org/";
    private SeekBar seekBar;
    private ArrayList<FrameLayout> boxes;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(255);
        boxes = new ArrayList<FrameLayout>();
        boxes.add((FrameLayout)findViewById(R.id.box1));
        boxes.add((FrameLayout)findViewById(R.id.box2));
        boxes.add((FrameLayout)findViewById(R.id.box3));
        boxes.add((FrameLayout)findViewById(R.id.box4));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int color;
                for (FrameLayout box : boxes) {
                    color = ((ColorDrawable) box.getBackground()).getColor();
                    color = Color.argb(255, Color.red(color),
                            Color.green(color), (int) (255 - i));
                    box.setBackgroundColor(color);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_information) {
            AlertDialog.Builder info = new AlertDialog.Builder(context);
            info.setTitle("Information")
                    .setMessage("Inspired by the works of artists such as " +
                            "Piet Mondrian and Ben Nicholson\n\n" +
                            "Click below to learn more")
                    .setCancelable(false)
                    .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Visit MOMA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(URL_WWW_MOMA_ORG));
                            startActivity(intent);
                        }
                    })
                    .create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
