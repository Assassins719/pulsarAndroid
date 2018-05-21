package com.pulsar.android.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pulsar.android.Adapter.ViewPagerAdapter;
import com.pulsar.android.Fragment.HistoryAll;
import com.pulsar.android.Fragment.HistoryToken;
import com.pulsar.android.GlobalVar;
import com.pulsar.android.Models.HistoryItem;
import com.pulsar.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;

public class DashboardActivity extends AppCompatActivity {
    int TO_HISTORY = 101;
    Bitmap bmp_qr= null;
    ProgressDialog dialog;
    View v_dashboard, v_swap, v_history, v_more;
    RadioGroup rg_tabs;
    TextView tx_greeting;
    int nPageIndex = 0;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);
        nPageIndex = getIntent().getIntExtra("mPage",0);
        findviews();
        initView();
    }

    public void findviews(){
        v_dashboard = findViewById(R.id.view_dashboard);
        v_swap = findViewById(R.id.view_swap);
        v_more = findViewById(R.id.view_more);
        v_history = findViewById(R.id.view_history);
        rg_tabs = findViewById(R.id.rg_tabs);

        tx_greeting = findViewById(R.id.tx_greeting);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour<12){
            tx_greeting.setText("Good Morning!");
        }else if (hour<18){
            tx_greeting.setText("Good Afternoon!");
        }else{
            tx_greeting.setText("Good Evening!");
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        rg_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId){
                    case R.id.rb_dash:
                        v_dashboard.setVisibility(View.VISIBLE);
                        v_swap.setVisibility(View.GONE);
                        v_history.setVisibility(View.GONE);
                        v_more.setVisibility(View.GONE);
                        break;
                    case R.id.rb_swap:
                        v_dashboard.setVisibility(View.GONE);
                        v_swap.setVisibility(View.VISIBLE);
                        v_history.setVisibility(View.GONE);
                        v_more.setVisibility(View.GONE);
                        break;
                    case R.id.rb_history:
                        v_dashboard.setVisibility(View.GONE);
                        v_swap.setVisibility(View.GONE);
                        v_history.setVisibility(View.VISIBLE);
                        v_more.setVisibility(View.GONE);
                        break;
                    case R.id.rb_more:
                        v_dashboard.setVisibility(View.GONE);
                        v_swap.setVisibility(View.GONE);
                        v_history.setVisibility(View.GONE);
                        v_more.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void getHistory(){
        RequestQueue chckConfirmReque = Volley.newRequestQueue(this);
        String url = GlobalVar.BASE_URL + "/transactions/address/" + GlobalVar.strAddress + "/limit/100";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            JSONArray mData = response.getJSONArray(0);
                            GlobalVar.mHistoryData.clear();
                            GlobalVar.mHistoryDataAll.clear();
                            for(int i=0;i<mData.length();i++){
                                JSONObject mItem = mData.getJSONObject(i);
                                HistoryItem mTemp = new HistoryItem(
                                        mItem.getString("recipient"),
                                        mItem.getString("sender"),
                                        mItem.getString("id"),
                                        mItem.getString("assetId"),
                                        mItem.getString("feeAsset"),
                                        mItem.getString("attachment"),
                                        mItem.getLong("timestamp"),
                                        mItem.getLong("amount"),
                                        mItem.getLong("fee"),
                                        false
                                );
                                GlobalVar.mHistoryData.add(mTemp);
                            }
                            GlobalVar.mHistoryDataAll = new ArrayList<>(GlobalVar.mHistoryData);
                            mHistoryToken.updateList();
                            mHistoryAll.updateList();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("errr","err");

                    }
                }
        );
        chckConfirmReque.add(jsonArrayRequest);
    }
    public void getUnconfirm(){
        RequestQueue chckConfirmReque = Volley.newRequestQueue(this);
        String url = GlobalVar.BASE_URL + "/transactions/unconfirmed/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
//                    JSONArray mData = response.getJSONArray(0);
                    GlobalVar.mUnconfirmedData.clear();
                    GlobalVar.mUnconfirmedDataAll.clear();
                    for(int i=0;i<response.length();i++){
                        JSONObject mItem = response.getJSONObject(i);
                        HistoryItem mTemp = new HistoryItem(
                                mItem.getString("recipient"),
                                mItem.getString("sender"),
                                mItem.getString("id"),
                                mItem.getString("assetId"),
                                mItem.getString("feeAsset"),
                                mItem.getString("attachment"),
                                mItem.getLong("timestamp"),
                                mItem.getLong("amount"),
                                mItem.getLong("fee"),
                                true
                        );
                        if(mTemp.getStrSender().equals(GlobalVar.strAddress) || mTemp.getStrReceipt().equals(GlobalVar.strAddress)) {
                            GlobalVar.mUnconfirmedData.add(mTemp);
                        }
                    }
                    GlobalVar.mUnconfirmedDataAll = new ArrayList<>(GlobalVar.mUnconfirmedData);
                    mHistoryToken.updateList();
                    mHistoryAll.updateList();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("errr","err");

                    }
                }
        );
        chckConfirmReque.add(jsonArrayRequest);
    }
    HistoryToken mHistoryToken = new HistoryToken();
    HistoryAll mHistoryAll = new HistoryAll();
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mHistoryToken, "Token Transactions");
        adapter.addFragment(mHistoryAll, "All Transactions");
        viewPager.setAdapter(adapter);
    }

    public void initView() {
        getHistory();
        getUnconfirm();
        dialog = ProgressDialog.show(DashboardActivity.this, "",
                "Please wait...", true);
        new Thread(new Runnable() {
            public void run(){
                String strAddress = GlobalVar.strAddressEncrypted;
                try {
                    bmp_qr = encodeAsBitmap(strAddress);
                    dialog.dismiss();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showPage(){
        switch (nPageIndex){
            case 0:
                v_dashboard.setVisibility(View.VISIBLE);
                v_swap.setVisibility(View.GONE);
                v_history.setVisibility(View.GONE);
                v_more.setVisibility(View.GONE);
                break;
            case 1:
                v_dashboard.setVisibility(View.GONE);
                v_swap.setVisibility(View.VISIBLE);
                v_history.setVisibility(View.GONE);
                v_more.setVisibility(View.GONE);
                break;
            case 2:
                v_dashboard.setVisibility(View.GONE);
                v_swap.setVisibility(View.GONE);
                v_history.setVisibility(View.VISIBLE);
                v_more.setVisibility(View.GONE);
                break;
            case 3:
                v_dashboard.setVisibility(View.GONE);
                v_swap.setVisibility(View.GONE);
                v_history.setVisibility(View.GONE);
                v_more.setVisibility(View.VISIBLE);
                break;
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        int WIDTH = 500;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? TRANSPARENT: getResources().getColor(R.color.colorPrimary);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }
    @Override
    public void onResume() {
        super.onResume();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour<12){
            tx_greeting.setText("Good Morning!");
        }else if (hour<18){
            tx_greeting.setText("Good Afternoon!");
        }else{
            tx_greeting.setText("Good Evening!");
        }
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_qrcode);
        LinearLayout lyt_dlg = dialog.findViewById(R.id.lyt_qr);
        ImageView img_qr = dialog.findViewById(R.id.img_qr);
        img_qr.setImageBitmap(bmp_qr);
        lyt_dlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void showQRDlg(View view)
    {
        showDialog(this);
    }

    public void gotoSendAcitivty(View view)
    {
        Intent intent = new Intent(DashboardActivity.this, SendActivity.class);
        startActivityForResult(intent, TO_HISTORY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_HISTORY) {
            if (resultCode == TO_HISTORY) {
                int nCard =data.getIntExtra("Card", 0);
                mHistoryToken.nCardType = nCard;
                if(mHistoryToken.ultraViewPager != null) {
                    mHistoryToken.ultraViewPager.setCurrentItem(nCard);
                }
                getHistory();
                getUnconfirm();
                nPageIndex = 2;
                showPage();
            }
        }
    }

    public void otherWallets(View view){

    }
    public void gotoMoreRedeem(View view){

    }
    public void gotoMoreOffers(View view){

    }
    public void gotoMoreMerchants(View view){

    }
    public void gotoMoreSetting(View view){
        Intent intent = new Intent(DashboardActivity.this, SettingActivity.class);
        startActivity(intent);
    }
    public void gotoMoreContact(View view){

    }
    public void gotoMoreLegal(View view){

    }
    public void gotoMoreFaqs(View view){

    }
    public void gotoMorePrivacy(View view){

    }
    public void gotoHistoryReceive(View view){

    }
    public void gotoHistorySend(View view){

    }

}


