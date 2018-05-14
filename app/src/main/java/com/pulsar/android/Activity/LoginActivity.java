package com.pulsar.android.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.pulsar.android.GlobalVar;
import com.pulsar.android.R;

public class LoginActivity extends AppCompatActivity {
    TextView tx_login;
    EditText ed_pwd;
    Button btn_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
    }
    public void initView(){
        ed_pwd = findViewById(R.id.edt_pwd);
        btn_continue = findViewById(R.id.bn_login);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPwd = String.valueOf(ed_pwd.getText());
                if(strPwd.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter password.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(strPwd.equals(GlobalVar.strPwd)){
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Password not match.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
