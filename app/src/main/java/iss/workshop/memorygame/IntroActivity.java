package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TextView tutorial = (TextView)findViewById(R.id.Tutorialtext);
        tutorial.setOnClickListener(this);
        TextView skiptutorial = (TextView)findViewById(R.id.skiptutorialtext);
        skiptutorial.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.Tutorialtext){
            final Intent intent=new Intent(this,Tutorial.class);
            startActivity(intent);
        }

        if(view.getId()==R.id.skiptutorialtext){
            final Intent intent= new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

}