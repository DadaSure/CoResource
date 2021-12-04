package com.shuo.coresource;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shuo.coresource.data.CardDAO;
import com.shuo.coresource.data.DbOpenHelper;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login_submit;
    private EditText et_login_username;
    private EditText et_login_password;
    private DbOpenHelper dbOpenHelper;
    private CardDAO cardDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        btn_login_submit=findViewById(R.id.btn_login_submit);
        et_login_username=findViewById(R.id.et_login_username);
        et_login_password=findViewById(R.id.et_login_password);

        dbOpenHelper=new DbOpenHelper(this);
        cardDAO=new CardDAO();

        btn_login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        String userName=et_login_username.getText().toString();
        String userPassword=et_login_password.getText().toString();

        String getPassword=cardDAO.readUserPassword(dbOpenHelper, userName);

        if(userPassword.equals(getPassword)){
            Toast.makeText(this,"登陆成功！",new Integer(2)).show();

            Log.i("login","login success");

            saveUsername(this,userName);

            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

            Timer timer=new Timer();
            timer.schedule(task,2100);

        }else {
            Toast.makeText(this,"用户名或密码错误",new Integer(2)).show();
        }
    }

    public void saveUsername(Context context, String username){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("username", username);
        editor.putBoolean("isLogin",true);
        Log.i("login","saved username: "+username);
        Log.i("login","saved isLogin:true");
        editor.commit();
    }
}
