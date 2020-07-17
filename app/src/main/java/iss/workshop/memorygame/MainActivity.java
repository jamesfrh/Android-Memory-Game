package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static iss.workshop.memorygame.Utilities.hideKeyBoardOutsideEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> selectedImgUrlList;
    private boolean downloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedImgUrlList = new ArrayList<>();

        Button fetchBtn = findViewById(R.id.fetchButton);
        fetchBtn.setOnClickListener(this);
        TextView myDownloadText = (TextView) findViewById(R.id.downloadText);
        myDownloadText.setText("");

    }

    @Override
    public void onClick(View v) {
        hideKeyBoardOutsideEditText(MainActivity.this);
        int id = v.getId();
        if (id == R.id.fetchButton) {
            final Context that = this;
            final GridLayout gridLayout = (GridLayout) findViewById(R.id.table);

            downloading = true;
            gridLayout.removeAllViews();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "https://stocksnap.io/search/nature";

                    //Uncomment this to allow user to type URL instead of hardcoding
                    /*
                    urlText = findViewById(R.id.urlText);
                    String urlToString = "";
                    if(urlText != null){
                        urlToString = urlText.getText().toString();
                    }
                    */
                    List<String> listTest = getImageUrls(url);

                    final ProgressBar myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
                    myProgressBar.setProgress(0);
                    selectedImgUrlList.clear();

                    Integer i = 0;
                    for (final String item : listTest) {
                        if (downloading) {
                            final GridLayout.LayoutParams myLayoutParams = new GridLayout.LayoutParams();
                            final ImageView image = new ImageView(that);
                            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            myLayoutParams.width = gridLayout.getWidth() / 4;
                            myLayoutParams.height = gridLayout.getWidth() / 4;
                            image.setPadding(0, 0, 0, 0);

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
                                        //hides keyboard after clicking images
                                        hideKeyBoardOutsideEditText(MainActivity.this);
                                        if (selectedImgUrlList.size() < 6 && !selectedImgUrlList.contains(item)) {
                                            selectedImgUrlList.add(item);
                                            //reduce opacity of image to indicate it has been clicked
                                            image.setAlpha(0.3f);
                                        }
                                        if (selectedImgUrlList.size() == 6) {
                                            //countdown animation
                                            final Button fetchbtn= (Button) findViewById(R.id.fetchButton);
                                            fetchbtn.setVisibility(View.INVISIBLE);
                                            final Button cancelbtn=(Button) findViewById(R.id.cancel);
                                            cancelbtn.setVisibility(View.INVISIBLE);

                                            View backgroundcolour= (View) findViewById(R.id.colorbackground);
                                            backgroundcolour.bringToFront();
                                            backgroundcolour.setVisibility(View.VISIBLE);

                                            final com.airbnb.lottie.LottieAnimationView countdownanimation= findViewById(R.id.countdownanimation);
                                            countdownanimation.bringToFront();
                                            countdownanimation.setVisibility(View.VISIBLE);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    countdownanimation.setVisibility(View.INVISIBLE);
                                                    final com.airbnb.lottie.LottieAnimationView animation= findViewById(R.id.gameboyanimation);
                                                    animation.bringToFront();
                                                    animation.setVisibility(View.VISIBLE);
                                                    sendimgs();
                                                }
                                            }, 5000);

                                        }
                                    }
                                });
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        gridLayout.addView(image, idx);
                                        //ProgressBar myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
                                        myProgressBar.setVisibility(View.VISIBLE);
                                        myProgressBar.incrementProgressBy(5);
                                        Integer progress = myProgressBar.getProgress();
                                        String downloadText = "Downloading " + progress / 5 + " of 20 images";
                                        if (progress == 100) {
                                            downloadText = "Download Completed!";
                                        }
                                        TextView myDownloadText = (TextView) findViewById(R.id.downloadText);
                                        myDownloadText.setText(downloadText);
                                    }
                                });
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //toast start
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Select 6 images to start the game!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
        }
    }

    private void sendimgs() {

        byte[] ByteArray = null;
        //sending list
        new Thread(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(MainActivity.this, GameActivity.class);
                for (int y = 0; y < selectedImgUrlList.size(); y++) {
                    //getbitmapfrom url
                    try {
                        URL url = new URL(selectedImgUrlList.get(y));
                        Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        // each image sent over as bmp[i]
                        i.putExtra("bmp" + y, byteArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(i);
            }
        }).start();
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

    public void onClickCancel(View view) {
        downloading = false;
    }


}