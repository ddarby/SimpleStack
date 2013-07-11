package com.ddarby.letterlatter2;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends Activity {
    TableLayout alphabetTL;
    Button textToReplace;
    MediaPlayer mPlayer;
    TextView musicMarquee;
    ArrayAdapter<CharSequence> spinnerAdapter;
    HashMap<Character, Set<String>> bigguy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ll2_main_layout);


        //ALL READING IN FILE AND SETTING "bigguy" STRUCTURE
        InputStream in = null;
        BufferedReader br;
        bigguy = new HashMap<Character, Set<String>>();
        try {
            in = getResources().openRawResource(R.raw.alphabet_set);
            br = new BufferedReader(new InputStreamReader(in));
            bigguy = new HashMap<Character, Set<String>>();
            String s;
            while (!(s = br.readLine()).equals("-1")) {
                Set<String> set = new HashSet<String>();
                String[] temp = s.split(" +");
                Character letter = temp[0].charAt(0);
                for (String str : temp) {
                    set.add(str);
                }
                bigguy.put(letter, set);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        //PRINTS ALL WORDS
        /*Set<Map.Entry<Character,Set<String>>> entrySet = bigguy.entrySet();
        for (Map.Entry<Character,Set<String>> entry : entrySet) {// iterates through ALL 26 [DONT HAVE TO!!!]
            for(String str : entry.getValue()){
                Log.d(entry.getKey().toString(),str);
            }
        }*/
        /*SETTING UP ALPHABET SCROLL*/
        View.OnClickListener alphabetListener;
        alphabetListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToReplace.setText(((TextView) view).getText());
                alphabetTL.setVisibility(View.INVISIBLE);

            }
        };

        alphabetTL = (TableLayout) findViewById(R.id.alphabet_scroll_wheel);
        TableRow alphabetRow = new TableRow(this);
        Button alphabetButton;

        for (int i = 0; i < 26; i++) {
            if (i % 5 == 0 && i != 0) {
                alphabetTL.addView(alphabetRow);
                alphabetRow = new TableRow(this);
            }
            alphabetButton = new Button(this);
            int temp = i + 65;
            char ch = (char) temp;
            //Log.d("alphabet_test","["+(ch)+"]");
            alphabetButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            alphabetButton.setText(ch + "");
            alphabetButton.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            alphabetButton.setOnClickListener(alphabetListener);
            alphabetRow.addView(alphabetButton);

            if (i == 25) {//Unnecessarily in this for loop
                Button closeButton = new Button(this);
                closeButton.setText("Close");
                alphabetRow.addView(closeButton);
                alphabetTL.addView(alphabetRow);
            }
        }





        /*SETTING UP FOUR LETTER GROUPING*/
        TextView tv1 = (TextView) findViewById(R.id.fristLetter);
        TextView tv2 = (TextView) findViewById(R.id.secondLetter);
        TextView tv3 = (TextView) findViewById(R.id.thirdLetter);
        TextView tv4 = (TextView) findViewById(R.id.fourthLetter);

        tv1.setOnClickListener(fourLetterOnclick());
        tv2.setOnClickListener(fourLetterOnclick());
        tv3.setOnClickListener(fourLetterOnclick());
        tv4.setOnClickListener(fourLetterOnclick());





        /*SETTING UP SCROLLING MARQUEE FOR MUSIC?? OR INFO??*/
        TextView txtView = (TextView) findViewById(R.id.music_marquee);
        txtView.setSelected(true);
        txtView.setSelectAllOnFocus(true);





        /*CHRONOMETER STUFF*/
        Chronometer timer = (Chronometer) findViewById(R.id.gameChronometer);
        timer.setFormat("%s");
        timer.start();




        /*SETTING UP FOR LEFT AND RIGHT LIST PANES*/
        ListView leftPaneLV = (ListView) findViewById(R.id.leftListPane);
        ListView rightPaneLV = (ListView) findViewById(R.id.rightListPane);

        ArrayList<String> leftList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            leftList.add("ABCD");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leftList);
        leftPaneLV.setAdapter(adapter);
        rightPaneLV.setAdapter(adapter);





        /*SETTING UP MUSIC*/
        musicMarquee = (TextView) findViewById(R.id.music_marquee);
        mPlayer = MediaPlayer.create(this, R.raw.chill_revolution);
        getResources().getResourceName(R.raw.chill_revolution);

        mPlayer.start();
        mPlayer.setLooping(true);

        Spinner musicSpinner = (Spinner) findViewById(R.id.music_spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.track_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        musicSpinner.setAdapter(spinnerAdapter);
/*
        //DUNNUNU DUNNU DUNNUUUNN
        musicSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                parent.getItemAtPosition(pos);
            }
        });
*/
    }

    @Override
    public void onDestroy() {
        Log.d("WILL THIS PRINT?", "HAS BEEN DESTROYED!!");
        mPlayer.stop();
        super.onDestroy();
    }

    public View.OnClickListener fourLetterOnclick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alphabetTL.setVisibility(alphabetTL.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                textToReplace = (Button) view;
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
