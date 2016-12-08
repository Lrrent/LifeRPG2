package com.example.da.liferpg;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.da.liferpg.database.DataBase;


public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText userPassword;
    Button login;
    CardView cv;
    FloatingActionButton addChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllView();
        DataBase dataBase = new DataBase();
        Log.i(dataBase.connect()+"", "onCreate: ");
        addChange.setOnClickListener(new myClickListener());
        login.setOnClickListener(new myClickListener());
    }
    private void findAllView(){
        addChange = (FloatingActionButton) findViewById(R.id.fab);
        login = (Button) findViewById(R.id.login);
        cv = (CardView) findViewById(R.id.cv);
    }
    class myClickListener implements View.OnClickListener {   //
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab:   //转换到注册界面
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                 //   if (Build >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, addChange, view.getTransitionName());
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                   // } else {
                    //    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                   // }
                    break;
                case R.id.login:
                    Explode explode = new Explode();
                    explode.setDuration(500);
                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                    Intent i2 = new Intent(LoginActivity.this,LoginSuccessActivity.class);
                    startActivity(i2, oc2.toBundle());
                    break;
            }
        }
    }
}
