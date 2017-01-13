package com.example.da.liferpg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.da.liferpg.database.DataBase;

import java.sql.SQLException;


public class RegisterActivity extends Activity {

    FloatingActionButton fab;
    CardView cvAdd;
    private EditText userName;
    private EditText userPassword;
    private EditText repeatPassword;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findAllView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {animateRevealClose();startActivity(new Intent(RegisterActivity.this, LoginActivity.class));}
        });
        register.setOnClickListener(new registerListener());
    }
    //register��ť�ļ����
    class registerListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String name = userName.getText().toString();
            String password = userPassword.getText().toString();
            String repeatPass = repeatPassword.getText().toString();
            if(checkInput(name,password,repeatPass)){ //����û���⣬���Կ��ǲ���,����Ҫ����Ƿ��Ѿ������û���
                checkInsert(name,password);  //��鲢�Ҳ鿴�û����Ƿ��ظ�,�Ƿ���Բ������ݿ�,�������߳�
            }
        }
        private boolean checkInput(String name,String password,String repeatPass){
            boolean checked = true;
            if (name.equals("")){userName.setError("�û���Ϊ��");checked = false;}
            if (password.equals("")){userPassword.setError("����Ϊ��"); checked = false;}
            if (repeatPass.equals("")){repeatPassword.setError("���ٴ���������"); checked = false;}
            if (checked == true && !password.equals(repeatPass)){
                Toast.makeText(getApplicationContext(),"�����������벻һ��",Toast.LENGTH_LONG).show(); checked = false;}
            return checked;
        }
        private void checkInsert(String name,String password){
            new DBAsyncTask().execute(name,password);
        }
    }
    public boolean checkNetwork(){ //����Ƿ�������
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){ //�ҵ����粢�����������
            return true;
        }
        else {
            return false;
        }
    }
    //�½��߳�,�������ݿ�,
    class DBAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) { //���߳�,�������ݿ�
            String name = params[0];
            String password = params[1];
            DataBase database = new DataBase();
            database.connect();
            try {
               // Log.i(name, "doInBackground: ");
                if (!checkNetwork()) { //���粻����
                    return "noNetwork";
                }
                else if (database.QueryExisted(name)){ //�û����Ѿ�����
                    return "existed";
                }
                else{ //����ע��,���û������������ݲ���user�����,Ȼ��֪ͨUI���Ե�¼
                    String sql = "INSERT INTO `user` (`userId`, `userName`, `userPassword`) VALUES (NULL, '"+name+"', '"+password+"');";
                    String rankingSql = "INSERT INTO `ranking` VALUES ('"+name+"',0,0,0,0,0);";
                   // Log.i(rankingSql, "doInBackground: ");
                    SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    database.Insert(sql);
                    database.Insert(rankingSql);
                    return "register";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) { //�Կؼ����д���
           // Log.i(s, "onPostExecute: ");
            if (s.equals("noNetwork")){
                Snackbar.make(userName,"���粻����",1000).setAction("����", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
            }
            else if (s.equals("existed")){
                Toast.makeText(getApplicationContext(),"�û����Ѿ�����",Toast.LENGTH_LONG).show();
            }
            else if (s.equals("register")){ //ע��ɹ�,��ת�ص�½����
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                RegisterActivity.this.finish();
                startActivity(intent);
            }
        }
    }
    private void findAllView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cvAdd = (CardView) findViewById(R.id.cv_add);
        userName = (EditText) findViewById(R.id.et_username);
        userPassword = (EditText) findViewById(R.id.et_password);
        repeatPassword = (EditText) findViewById(R.id.et_repeatpassword);
        register = (Button) findViewById(R.id.register);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }
            @Override
            public void onTransitionEnd(Transition transition) {}
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}});
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(android.R.drawable.btn_plus);
                RegisterActivity.super.onBackPressed();
            }
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
