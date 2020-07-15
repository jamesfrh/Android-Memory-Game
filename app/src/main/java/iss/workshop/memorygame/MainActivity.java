package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gitcommit test

    }

    @Override
    public void onClick(View v){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String url = "https://stocksnap.io/search/nature";
                List<String> listTest = getImageUrls(url);
                for (String item:listTest) {
                    System.out.println(item);
                }
            }
        }).start();
    }
    protected void downloadImages(String target){

    }

    protected List<String> getImageUrls(String urlString)  {

        List<String> imageUrlList = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            httpConn.addRequestProperty("User-Agent", "Mozilla/4.76");
            httpConn.connect();

            int responseCode = httpConn.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("img src") && inputLine.contains(".jpg")) {

                        String s = "<img src=\"";
                        int urlPosition = inputLine.indexOf(s)+s.length();
                        String imgUrl = inputLine.substring(urlPosition, inputLine.indexOf("\"", urlPosition+1));

                        imageUrlList.add(imgUrl);
                        //String imgSrcString = inputLine.substring(inputLine.indexOf("img src=") + 9, inputLine.indexOf(".jpg") + 4);
                        //ImageList.add(imgSrcString);
                    }
                }
                in.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return imageUrlList;
    }



}