package de.fablab.ara.arapirate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Watch extends Activity {

    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private ArrayList<Channel> channels;

    private int[] channelColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        final ListView channelList = (ListView) findViewById(R.id.channelList);

        channelColors = new int[] { Color.BLACK, Color.parseColor("#33B5E5"),
                Color.parseColor("#AA66CC"), Color.parseColor("#99CC00"), Color.parseColor("#FFBB33"),
                Color.parseColor("#FF4444") };

        channels = new ArrayList<Channel>();

        for (int i = 0; i < channelColors.length; i++) {
            channels.add(new Channel(channelColors[i]));
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, channels);
        channelList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.watch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class StableArrayAdapter extends ArrayAdapter<Channel> {

        private final Context context;

        HashMap<Channel, Integer> mIdMap = new HashMap<Channel, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<Channel> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;

            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            Channel item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.channel_row, parent, false);

            ImageButton colorSelector = (ImageButton) rowView.findViewById(R.id.channel_color);
            ImageButton contextMenu = (ImageButton) rowView.findViewById(R.id.context_menu);

            contextMenu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final String[] items = getResources().getStringArray(R.array.channel_row_context_actions);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.action_select_trigger);
                    builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();

                            // TODO
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            return rowView;
        }
    }
}
