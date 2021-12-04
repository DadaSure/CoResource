package com.shuo.coresource;

import com.shuo.coresource.data.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shuo.coresource.data.DbOpenHelper;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_reg_submit;
    private EditText et_reg_username;
    private EditText et_reg_password;
    private DbOpenHelper dbOpenHelper;
    private CardDAO cardDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        btn_reg_submit=findViewById(R.id.btn_reg_submit);
        et_reg_username=findViewById(R.id.et_reg_username);
        et_reg_password=findViewById(R.id.et_reg_password);

        dbOpenHelper=new DbOpenHelper(this);

        cardDAO=new CardDAO();

        btn_reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register(){
        boolean isExisted = cardDAO.isUserExist(dbOpenHelper, et_reg_username.getText().toString());
        
        boolean isUsernameValid=true;
        boolean isPasswordValid=true;
        
        String username=et_reg_username.getText().toString();
        String passowrd=et_reg_password.getText().toString();

        if(passowrd.contains("*") || passowrd.contains("#") || passowrd.contains("!") || passowrd.contains("@") || passowrd.contains("$") || passowrd.contains(" ") || passowrd.contains("&") || passowrd.length()==0 || passowrd.length()>20){
            isPasswordValid=false;
        }

        if(username.contains("*") || username.contains("#") || username.contains("!") || username.contains("@") || username.contains("$") || username.contains(" ") || username.contains("&") || username.length()==0 || username.length()>20){
            isUsernameValid=false;
        }

        boolean isValid=(isPasswordValid&&isUsernameValid);

        if(isExisted){
            Toast.makeText(this,"用户名已存在！",new Integer(2)).show();
        }else if(!isValid){
            Toast.makeText(this,"输入格式有误！",new Integer(2)).show();
        }else {
            cardDAO.createUser(dbOpenHelper,et_reg_username.getText().toString(),et_reg_password.getText().toString());
            Toast.makeText(this,"注册成功！",new Integer(2)).show();

            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            };

            Timer timer=new Timer();
            timer.schedule(task,2100);
        }

    }
}
