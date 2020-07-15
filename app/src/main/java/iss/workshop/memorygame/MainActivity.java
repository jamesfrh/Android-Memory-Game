package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> selectedImgUrlList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gitcommit test
        selectedImgUrlList = new ArrayList<String>();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fetchButton) {
            final Context that = this;
            final GridLayout gridLayout = (GridLayout) findViewById(R.id.table);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "https://stocksnap.io/search/nature";
                    List<String> listTest = getImageUrls(url);
                    Integer i = 0;

                    for (final String item : listTest) {
                        final GridLayout.LayoutParams myLayoutParams = new GridLayout.LayoutParams();
                        final ImageView image = new ImageView(that);

                        try {
                            URL imageurl = new URL(item);
                            Bitmap bmp = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
                            image.setImageBitmap(bmp);
                            image.setLayoutParams(myLayoutParams);

                            final Integer idx = i;
                            i++;
                            image.setId(i);
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int id = v.getId();
                                    String imgURL = item;
                                    if(selectedImgUrlList.size() < 6 && !selectedImgUrlList.contains(imgURL)){
                                        selectedImgUrlList.add(imgURL);
                                        //fade the current image
                                    }
                                    if(selectedImgUrlList.size() == 6){
                                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                        intent.putStringArrayListExtra("urlList", selectedImgUrlList);
                                        startActivity(intent);

                                    }
                                }
                            });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gridLayout.addView(image, idx);
                                }
                            });
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


    protected void downloadImages(String target) {

    }

    protected List<String> getImageUrls(String urlString) {

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
                    System.out.println(inputLine);
                    if (inputLine.contains("img src") && inputLine.contains(".jpg")) {

                        String s = "<img src=\"";
                        int urlPosition = inputLine.indexOf(s) + s.length();
                        String imgUrl = inputLine.substring(urlPosition, inputLine.indexOf("\"", urlPosition + 1));

                        imageUrlList.add(imgUrl);
                        if (imageUrlList.size() == 20) {
                            break;
                        }
                        //String imgSrcString = inputLine.substring(inputLine.indexOf("img src=") + 9, inputLine.indexOf(".jpg") + 4);
                        //ImageList.add(imgSrcString);
                    }
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrlList;
    }


}