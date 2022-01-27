package com.example.parsexml_asynctask;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list;
    ArrayList<Data> ListData = new ArrayList<>();
    String Url_Top_10Songs = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml";
    String Url_Top_10Movies = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";
    String Url_Top_10Albums="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=10/xml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        Parse_AsyncTask parse = new Parse_AsyncTask();
        parse.execute(Url_Top_10Songs);
    }
    public class Parse_AsyncTask extends AsyncTask<String,Void,ArrayList<Data>> {
        URL url;
        Data currentData =null;
        @Override
        protected ArrayList<Data> doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                xpp.setInput(getInputStream(url), "UTF_8");
                int eventType = xpp.getEventType();
                String name,artist,image;
                float price;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String eltName = null;
                    switch(eventType)
                    {
                        case XmlPullParser.START_TAG:
                            eltName = xpp.getName();
                            if("entry".equals(eltName)){
                                currentData = new Data();
                                ListData.add(currentData);
                            }else if(currentData != null) {
                                if("im:name".equals(eltName)){
                                    name=xpp.nextText().toString();
                                    currentData.setName(name);
                                }else if("im:artist".equals(eltName)){
                                    artist=xpp.nextText().toString();
                                    currentData.setArtist(artist);
                                }else if("im:image".equals(eltName)){
                                    image=xpp.nextText().toString();
                                    currentData.setUrl_image(image);
                                }else if("im:price".equals(eltName)){
                                    price=Float.parseFloat(xpp.getAttributeValue(null,"amount"));
                                    currentData.setPrice(price);
                                }else { skip(xpp); }
                            }break;
                        default:break;
                    }
                    eventType=xpp.next();
                }
            } catch (MalformedURLException e) {
                Log.e("doInBackground","MalformedURLException");
            } catch (XmlPullParserException e) {
                Log.e("doInBackground","XmlPullParserException");
            } catch (IOException e) {
                Log.e("doInBackground","IOException");
            }
            return ListData;

        }

        private void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
            int count = 1;
            while (count != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        count--;
                        break;
                    case XmlPullParser.START_TAG:
                        count++;
                        break;
                }
            }
        }
        public InputStream getInputStream(URL url) {
            try {
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                Log.e("getInputStream","IOException");
                return null;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<Data> ListData) {
            super.onPostExecute(ListData);
            CustomListAdapter listAdapter = new CustomListAdapter(getApplicationContext(),R.layout.custom_list_layout,ListData);
            list.setAdapter(listAdapter);
        }
    }
}