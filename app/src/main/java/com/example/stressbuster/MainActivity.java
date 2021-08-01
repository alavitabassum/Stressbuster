package com.example.stressbuster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String urlJson = "https://official-joke-api.appspot.com/random_ten";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.txt);

        new backgroundTask().execute();
    }

public class backgroundTask extends AsyncTask<Void,Void,String>{

        ProgressDialog pd;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("Wait!");
        pd.setMessage("Downloading..");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder builder = null;
        try {
            URL url = new URL(urlJson);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            builder = new StringBuilder();
            while((line= bufferedReader.readLine())!=null){
                builder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // Log.e("Error",builder.toString());
        return builder.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pd.dismiss();

        StringBuilder stringBuilder = new StringBuilder();

        try {
            JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                String jokeid = object.getString("id");
                String type = object.getString("type");
                String setup = object.getString("setup");
                String punchline = object.getString("punchline");

               // stringBuilder.append(jokeid +"\n"+ type +"\n"+ setup +"\n"+ punchline +"\n");
                stringBuilder.append(setup +"\n"+ punchline +"\n\n");
                textView.setText(stringBuilder.toString());


                Log.e("JokeID",jokeid);
                Log.e("JokeType",type);
                Log.e("JokeInfo",setup);
                Log.e("Punchline",punchline);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
}
