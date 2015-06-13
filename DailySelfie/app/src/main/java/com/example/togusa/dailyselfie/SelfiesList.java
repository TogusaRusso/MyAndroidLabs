package com.example.togusa.dailyselfie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Bitmap;
import java.util.List;
import java.io.File;
import android.os.Environment;
import android.graphics.BitmapFactory;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.net.Uri;
import java.util.Date;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlarmManager;
import android.app.PendingIntent;


public class SelfiesList extends ActionBarActivity {
    private static final File WORKING_DIRECTORY =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private List<Selfie> selfies = new ArrayList<Selfie>();
    private static SelfieAdapter selfie_adapter;
    private static final int TAKE_SELFIE = 1;
    ListView list;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent notificationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfies_list);
        list = (ListView) findViewById(R.id.listView);
        loadList(selfies);
        selfie_adapter = new SelfieAdapter(this, R.layout.one_selfie, R.id.selfie_name, selfies);
        selfie_adapter.setNotifyOnChange(true);
        list.setAdapter(selfie_adapter);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent bigSelfie = new Intent(SelfiesList.this, FullscreenActivity.class);
                bigSelfie.putExtra("File", selfies.get(i).file);
                startActivity(bigSelfie);
            }
        });
        notificationIntent = new Intent(SelfiesList.this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SelfiesList.this, 0, notificationIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Broadcast the notification intent at specified intervals
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP
                , System.currentTimeMillis() + 120000L
                , 120000L
                , pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selfies_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_selfie){
            Intent takeSelfie = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takeSelfie.resolveActivity(getPackageManager()) != null) {

                File newSelfie = null;
                try {

                    String date_time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "slf_" + date_time;
                    WORKING_DIRECTORY.mkdirs();
                    newSelfie = File.createTempFile(
                            imageFileName
                            , ".jpg"
                            , WORKING_DIRECTORY
                    );
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Can't create image file",
                            Toast.LENGTH_SHORT).show();
                }

                if (newSelfie != null) {
                    takeSelfie.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newSelfie));
                    startActivityForResult(takeSelfie, TAKE_SELFIE);
                }

            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_SELFIE && resultCode == RESULT_OK) {
            loadList(selfies);
            selfie_adapter.notifyDataSetChanged();
        }
    }


    private void loadList(List <Selfie> selfies) {
        if (selfies != null && WORKING_DIRECTORY.exists()) {
            // There is no selfies yet.
            selfies.clear();
            for (File file : WORKING_DIRECTORY.listFiles()) {
                selfies.add(
                        new Selfie(
                                file.getName()
                                , file.getAbsolutePath()
                                , madeIcon(file.getAbsolutePath())
                        )
                );
            }

        }
    }

    private Bitmap madeIcon(String file) {

        // Get size of selfie
        BitmapFactory.Options bmo = new BitmapFactory.Options();
        bmo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bmo);
        int Height = bmo.outHeight;

        // Decode the image file into a Bitmap sized to fill the View
        bmo.inSampleSize = Height / 100;
        bmo.inJustDecodeBounds = false;


        return BitmapFactory.decodeFile(file, bmo);
    }

}





class SelfieAdapter extends ArrayAdapter<Selfie> {

    private final Context context;

    public SelfieAdapter(Context context, int resource, int textViewResource, List<Selfie> objects) {
        super(context, resource, textViewResource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Selfie selfie = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.one_selfie, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.selfie_name);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.selfie_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (selfie != null) {
            viewHolder.textView.setText(selfie.name.substring(4, 19));
            viewHolder.imageView.setImageBitmap(selfie.icon);
        }

        return convertView;
    }
}
class ViewHolder {
    TextView textView;
    ImageView imageView;
}

class Selfie {

    String name;
    String file;
    Bitmap icon;

    public Selfie(String name, String file, Bitmap icon) {
        this.name = name;
        this.file = file;
        this.icon = icon;
    }
}