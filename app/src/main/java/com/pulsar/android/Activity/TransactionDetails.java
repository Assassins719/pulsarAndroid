package com.pulsar.android.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Dash;
import com.pulsar.android.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDetails extends AppCompatActivity {
    ImageView top_img;
    EditText edt_address, edt_amount, edt_usd, edt_desc;
    TextView tx_fee_unit, tx_id,tx_confirm, tx_time, tx_status, tx_amount;
    String strRecipt, strId, strDesc, strHeight, strAmount;
    long nTime;
    int nCardType, nFeeType;
    boolean isUnconfirmed, isSend = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_send_success);
        strRecipt = getIntent().getStringExtra("receipient");
        strId = getIntent().getStringExtra("id");
        strDesc = getIntent().getStringExtra("description");
        nTime = getIntent().getLongExtra("timestamp", 0);
        isUnconfirmed = getIntent().getBooleanExtra("unconfirmed", false);
        isSend = getIntent().getBooleanExtra("isSend", true);
        strHeight = getIntent().getStringExtra("height");
        strAmount = getIntent().getStringExtra("amount");
        nCardType = getIntent().getIntExtra("cardid", 0);
        nFeeType = getIntent().getIntExtra("feeid", 0);
        initView();

    }
    public void initView(){
        top_img = findViewById(R.id.img_transaction_status);
        edt_address = findViewById(R.id.edt_address);
        edt_amount = findViewById(R.id.edt_amount);
        edt_usd = findViewById(R.id.edt_converted);
        edt_desc = findViewById(R.id.edt_desc);
        tx_fee_unit = findViewById(R.id.tx_converted_tax);
        tx_id = findViewById(R.id.tx_txid);
        tx_confirm = findViewById(R.id.tx_confirm);
        tx_time = findViewById(R.id.tx_time);
        tx_status = findViewById(R.id.tx_result);
        tx_amount = findViewById(R.id.tx_amount);

        tx_amount.setText(strAmount);
        edt_address.setText(strRecipt);
        edt_amount.setText(strAmount);
        edt_desc.setText(strDesc);
        switch (nCardType){
            case 0:
                tx_amount.setText("Amount: ƒ");
                break;
            case 1:
                tx_amount.setText("Amount: ₦");
                break;
            case 2:
                tx_amount.setText("Amount: Ʉ");
                break;
        }
        switch (nFeeType){
            case 0:
                tx_fee_unit.setText("Favelas");
                break;
            case 1:
                tx_fee_unit.setText("₦ertia");
                break;
            case 2:
                tx_fee_unit.setText("UXSG");
                break;
        }
        if(isSend){
            top_img.setImageDrawable(getResources().getDrawable(R.drawable.img_send));
        }else{
            top_img.setImageDrawable(getResources().getDrawable(R.drawable.img_receive));
        }
        tx_id.setText(strId);
        edt_desc.setText(strDesc);

        Date mDate = new Date(nTime);
        DateFormat df = new SimpleDateFormat("dd:MM:yy, HH:mm");
        tx_time.setText(df.format(mDate));
    }
    public void gotoHistory(View view){
        Intent intent = new Intent(TransactionDetails.this, DashboardActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt("nPage",2);
        intent.putExtras(mBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.finish();
    }
    public void gotoBack(View view){
        Intent intent = new Intent(TransactionDetails.this, SendActivity.class);
        startActivity(intent);
        this.finish();
    }
}
