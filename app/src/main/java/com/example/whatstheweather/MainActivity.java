package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    public class Download extends AsyncTask<String,Void,String > {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpsURLConnection httpsURLConnection;
            try {
                url=new URL(urls[0]);
                httpsURLConnection=(HttpsURLConnection) url.openConnection();
                InputStream in =httpsURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                char c;
                String result="";
                while(data!=-1)
                {
                    c=(char) data;
                    result+=c;
                    data=reader.read();
                }
                return result;
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't find Weather", Toast.LENGTH_SHORT).show();
                Log.i("error", "doInBackground: ");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("json",s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weather=jsonObject.getString("weather");
                Log.i("weather",weather);

                JSONArray jsonArray=new JSONArray(weather);
                int i;
                String result="";
                for(i=0;i<jsonArray.length();i++)
                {
                    JSONObject ob=jsonArray.getJSONObject(i);
                    String main=ob.getString("main");
                    String description=ob.getString("description");
                    if(!main.equals("") && !description.equals("")) {
                        result += main + " : " + description;
                        result += '\n';
                    }
                }
                textView.setText(result);

            } catch (Exception e) {
                Log.i("error", "onPostExecute: ");
                Toast.makeText(getApplicationContext(), "Couldn't find Weather", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void getWeather(View view){
        Download download=new Download();
        try {
            String encoded= URLEncoder.encode(editText.getText().toString(),"UTF-8");
            String result=download.execute("https://openweathermap.org/data/2.5/weather?q="+encoded+"&appid=b6907d289e10d714a6e88b30761fae22").get();
            InputMethodManager manager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Couldn't find Weather", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView2);
    }
}
