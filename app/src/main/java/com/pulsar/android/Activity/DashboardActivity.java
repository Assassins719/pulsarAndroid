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
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

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

import static android.graphics.Color.TRANSPARENT;

public class DashboardActivity extends AppCompatActivity {
    Bitmap bmp_qr= null;
    ProgressDialog dialog;
    View v_dashboard, v_swap, v_history, v_more;
    RadioGroup rg_tabs;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);
        findviews();
        initView();
    }

    public void findviews(){
        v_dashboard = findViewById(R.id.view_dashboard);
        v_swap = findViewById(R.id.view_swap);
        v_more = findViewById(R.id.view_more);
        v_history = findViewById(R.id.view_history);
        rg_tabs = findViewById(R.id.rg_tabs);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for(int i=0;i<5;i++)
        {
            HistoryItem p=new HistoryItem("Afeaefaw"+i, "Eafewafwea"+i,"Eafewafwea"+i,"Eafewafwea"+i);
            GlobalVar.mHistoryData.add(p);
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HistoryToken(), "Token Transactions");
        adapter.addFragment(new HistoryAll(), "All Transactions");
        viewPager.setAdapter(adapter);
    }

    public void initView() {
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
        startActivity(intent);
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


