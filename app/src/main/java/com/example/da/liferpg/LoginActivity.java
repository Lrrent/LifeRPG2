package com.example.da.liferpg;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.da.liferpg.database.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText userPassword;
    Button login;
    CardView cv;
    FloatingActionButton addChange;
    SharedPreferences LoginedJudg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllView();
        addChange.setOnClickListener(new myClickListener());
        login.setOnClickListener(new myClickListener());
    }
    private void findAllView(){
        addChange = (FloatingActionButton) findViewById(R.id.fab);
        login = (Button) findViewById(R.id.login);
        cv = (CardView) findViewById(R.id.cv);
        userName = (EditText) findViewById(R.id.et_username);
        userPassword = (EditText) findViewById(R.id.et_password);
    }
    class DBAsyncTask extends AsyncTask<String,Integer,String>{  //访问外部数据库涉及联网,所以必须在另一个线程中执行
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            DataBase dataBase = new DataBase();  //连接到数据库中
            dataBase.connect();
            /*-------------------- 检查账号和密码是否正确------------------------*/
            try {
                boolean exited = dataBase.QueryExisted(name);
                if (!exited){   //不存在用户名
                    return "notExited";
                }
                else{
                    ResultSet rs = dataBase.Query("select * from user where userName='"+name+"';");
                    while (rs.next()){
                        //Log.i(rs.getString("userId"), "doInBackground: ");
                        String nameDB = rs.getString("userName");
                        String passwordDB =rs.getString("userPassword");
                        if (name.equals(nameDB) && password.equals(passwordDB)){ //可以登录
                            return "logined,"+name+","+password;
                        }
                        else if (!password.equals(passwordDB)){ //密码错误
                            return "wrongPassword";
                        }
                    }
                    dataBase.disConnect();   //断开数据库连接
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if (s.equals("notExited")){ //用户名不存在
                Toast.makeText(getApplicationContext(),"用户不存在",Toast.LENGTH_LONG).show();

               // SnackbarUtil.LongSnackbar(userName,"用户名不存在",0xff2195f3,0xffffffff).show();
            }
            else if (s.equals("wrongPassword")){
                userPassword.setError("密码错误");
            }
            else if (s.split(",")[0].equals("logined")){ //允许登陆
                LoginedJudg = getSharedPreferences("user",MODE_PRIVATE);  //可以登录之后想sharepreference中写入数据,下次就不需要再登陆
                SharedPreferences.Editor editor = LoginedJudg.edit();
                String loginFlag = "logined";
                editor.putString("logined",loginFlag);
                editor.putString("userName",s.split(",")[1]);
                editor.putString("userPassword",s.split(",")[2]);
                editor.commit();
                Explode explode = new Explode();
                explode.setDuration(500);
                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                Intent i2 = new Intent(LoginActivity.this,main_window.class);
                startActivity(i2, oc2.toBundle());
            }
        }
    }
    class myClickListener implements View.OnClickListener {   //按钮的监听事件
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab:   //转换到注册界面
                        getWindow().setExitTransition(null);
                        getWindow().setEnterTransition(null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, addChange, view.getTransitionName());
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                    break;
                case R.id.login:
                        String name = userName.getText().toString();
                        String password = userPassword.getText().toString();
                        if(checkInput(name,password)){ //当输入不为空
                            //需要访问但数据库,所以使用新建一个线程
                             checkedInsert(name,password);  //判断是否能够插入数据库,如可以,插入数据库中之后就可以跳转到主界面
                        }
                    break;
            }
        }
        private boolean checkedInsert(String name, String password){ //这里访问数据库,同时检查密码和用户名是否正确
            boolean inserted = true;
            new DBAsyncTask().execute(name,password);
            return inserted;
        }
        private boolean checkInput(String name,String password){ //检查输入是否正确(为空)
            boolean checked = true;
            if (name.equals("")){ //用户名为空时
                userName.setError("用户名为空");
                checked = false;
            }
            if (password.equals("")){ //密码为空
                userPassword.setError("密码不能为空");
                checked = false;
            }
            return checked;
        }
    }
}
