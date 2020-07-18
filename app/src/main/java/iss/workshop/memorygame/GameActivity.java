package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    MediaPlayer player;
    private TextView pairView=null;

    private int matchCount = 0;
    private int flipCardNum=0;
    public ArrayList<ImageView> compareImageList = new ArrayList<ImageView>();
    private ArrayList<Drawable> gameimages;
    private ArrayList<Drawable> duplicatedImages;
    private int coverImage = R.drawable.cover;
    private int[] imageViews = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4,
            R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8,
            R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12};
    private Chronometer timer;
    private int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        player = MediaPlayer.create(this,R.raw.time);
        player.start();
        player.setLooping(true);
        final ImageButton btnMusic=(ImageButton)findViewById(R.id.music);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!player.isPlaying()) {
                    player.start();
                    player.setLooping(true);
                    btnMusic.setImageResource(R.drawable.ic_baseline_music_off_24);
                } else {
                    player.pause();
                    btnMusic.setImageResource(R.drawable.ic_baseline_music_note_24);
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                    System.out.println("Running...");
            }
        }).start();

        pairView = (TextView) findViewById(R.id.score);

        ImageView view1=findViewById(R.id.imageView1);
        view1.setOnClickListener(this);

        ImageView view2=findViewById(R.id.imageView2);
        view2.setOnClickListener(this);

        ImageView view3=findViewById(R.id.imageView3);
        view3.setOnClickListener(this);

        ImageView view4=findViewById(R.id.imageView4);
        view4.setOnClickListener(this);

        ImageView view5=findViewById(R.id.imageView5);
        view5.setOnClickListener(this);

        ImageView view6=findViewById(R.id.imageView6);
        view6.setOnClickListener(this);

        ImageView view7=findViewById(R.id.imageView7);
        view7.setOnClickListener(this);

        ImageView view8=findViewById(R.id.imageView8);
        view8.setOnClickListener(this);

        ImageView view9=findViewById(R.id.imageView9);
        view9.setOnClickListener(this);

        ImageView view10=findViewById(R.id.imageView10);
        view10.setOnClickListener(this);

        ImageView view11=findViewById(R.id.imageView11);
        view11.setOnClickListener(this);

        ImageView view12=findViewById(R.id.imageView12);
        view12.setOnClickListener(this);

        // Load images, duplicate and shuffle
        loadimg();
        duplicatedImages = new ArrayList<Drawable>();
        duplicatedImages.addAll(gameimages);
        duplicatedImages.addAll(gameimages);
        Collections.shuffle(duplicatedImages);

        // Set cover image to all image views
        for (int i=0; i<imageViews.length; i++) {
            ImageView imageView = findViewById(imageViews[i]);
            imageView.setImageResource(coverImage);
            // imageView.setImageDrawable(duplicatedImages.get(i)); //use this to replace placeholder onClick
        }
    }

    private void loadimg(){
        gameimages= new ArrayList<Drawable>();
        for(int i =0;i<6;i++){
            Intent intent= getIntent();
            byte[] byteArray = intent.getByteArrayExtra("bmp"+i);
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Drawable drawable= new BitmapDrawable(getResources(),bm);
            gameimages.add(drawable);
        }
    }
    // Chronometer utilities
    public void timerStart(){
        timer = findViewById(R.id.chronometer);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    public void timerStop(){
        timer = findViewById(R.id.chronometer);
        timer.stop();
    }

    @Override
    public void onClick(View view) {
        clickCounter++;
        if(clickCounter == 1){
            timerStart();
        }
        final ImageView imageView=(ImageView) view;
        int currStatus=0;
        //get current card status (actualimage=1, cover=0)
        currStatus=showUpdatedFlipCard(imageView);
        final int finalCurrStatus = currStatus;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(finalCurrStatus ==1){
                    compareImageList.add(imageView);
                }
                //Compare two images if it's the same
                if(compareImageList.size()==2){
                    ImageView firstImage= compareImageList.get(0);
                    ImageView secondImage=compareImageList.get(1);
                    final Bitmap bitmap1=((BitmapDrawable)firstImage.getDrawable()).getBitmap();
                    final Bitmap bitmap2=((BitmapDrawable)secondImage.getDrawable()).getBitmap();

                    //if not the same
                    if(bitmap1!=bitmap2){
                        showUpdatedFlipCard(firstImage);
                        showUpdatedFlipCard(secondImage);
                        compareImageList.clear();
                    }
                    //same
                    else{
                        compareImageList.get(0).setClickable(false);
                        compareImageList.get(1).setClickable(false);
                        flipCardNum=0;
                        compareImageList.clear();
                        matchCount++;
                        updateCount();

                        //clappinganimation
                        final View backgroundcolour= (View)findViewById(R.id.colorbackground2);
                        backgroundcolour.bringToFront();
                        backgroundcolour.setVisibility(View.VISIBLE);
                        final com.airbnb.lottie.LottieAnimationView animation1 = findViewById(R.id.claphandanimation);
                        animation1.bringToFront();
                        animation1.setVisibility(View.VISIBLE);
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animation1.setVisibility(View.INVISIBLE);
                                backgroundcolour.setVisibility(View.INVISIBLE);
                            }
                        }, 1000);

                    }

                    if(matchCount==Constant.max_pairs)
                    {
                        timerStop();
                        // put the time into roundTime, for highscore
                        //Toast msg= Toast.makeText(GameActivity.this,"Congratulations!!",Toast.LENGTH_LONG);

                        //endinganimation
                        final View backgroundcolour1= (View)findViewById(R.id.gamecompletionbackground);
                        backgroundcolour1.bringToFront();
                        backgroundcolour1.setVisibility(View.VISIBLE);

                        final TextView gamecompletiontext= (TextView) findViewById(R.id.gamecompletiontext);
                        gamecompletiontext.bringToFront();
                        gamecompletiontext.setVisibility(View.VISIBLE);

                        final com.airbnb.lottie.LottieAnimationView fireworks= findViewById(R.id.fireworks);
                        fireworks.bringToFront();
                        fireworks.setVisibility(View.VISIBLE);

                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {returnMain();
                            }
                        }, 850);

                    }
                }
            }
        }, 500);
    }

    private void returnMain() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GameActivity.this, MainActivity.class));
            }
        }, 1000);
    }
    //check the current card and flip card (call flip card method)
    protected int showUpdatedFlipCard(ImageView imageView) {
        int currflipStatus;
        int updateFlipStatus=0;
        int index= (Integer.parseInt((String) imageView.getTag()))-1;

        //get current image name(tag)
        final Bitmap imageBitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        final Bitmap hidden = ((BitmapDrawable)getResources().getDrawable(R.drawable.cover)).getBitmap();


        if(imageBitmap==hidden){
            if(flipCardNum<2){
                currflipStatus=0;
                updateFlipStatus=flipCard(imageView,currflipStatus,index);
                flipCardNum++;
            }
        }
        else {
            currflipStatus = 1;
            updateFlipStatus=flipCard(imageView, currflipStatus,index);
            flipCardNum--;
            compareImageList.clear();
        }
        return updateFlipStatus;
    }

    //flip the card
    protected int flipCard(ImageView imageView,int flipStatus, int index) {
        if(flipStatus==0){
            imageView.setImageDrawable(duplicatedImages.get(index));
            flipStatus=1;
        }
        else{
            imageView.setImageResource(R.drawable.cover);
            flipStatus=0;
            compareImageList.clear();
        }
        return flipStatus;
    }

    @SuppressLint("StringFormatMatches")
    private void updateCount() {
        pairView.setText(getString(R.string.match_pair, matchCount, Constant.max_pairs));
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.seekTo(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}