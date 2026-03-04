/*
 **Copyright (C) 2017  xfalcon
 **
 **This program is free software: you can redistribute it and/or modify
 **it under the terms of the GNU General Public License as published by
 **the Free Software Foundation, either version 3 of the License, or
 **(at your option) any later version.
 **
 **This program is distributed in the hope that it will be useful,
 **but WITHOUT ANY WARRANTY; without even the implied warranty of
 **MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 **GNU General Public License for more details.
 **
 **You should have received a copy of the GNU General Public License
 **along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **
 */

package com.github.xfalcon.vhosts;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.github.xfalcon.vhosts.util.LogUtils;

import java.util.Objects;

public class DonationActivity extends AppCompatActivity {

    private final String TAG = DonationActivity.class.getSimpleName();
    
    // 修复：使用正确的 BuildConfig 字段名 IS_GooglePlay
    // 由于移除了 Google Play 支付，此值始终为 false
    private static final boolean IS_GooglePlay = BuildConfig.IS_GooglePlay;
    
    /**
     * PayPal
     */
    private static final String PAYPAL_ADDRESS = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=X2SCFSHBXUMUC&lc=GB&item_name=Donate&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest";

    /**
     * Bitcoin
     */
    private static final String BITCOIN_ADDRESS = "1JwQYRiGm7JBuWSaxuVrFvatHTwJ5mzAdm";

    /**
     * Alipay
     */
    private static final String APLPAY_QRADDRESS = "https://raw.githubusercontent.com/x-falcon/tools/master/a.png";
    private static final String APLPAY_ADDRESS = "https://qr.alipay.com/aex00155nkzbj7tuxj3vw38";
    private static final String APLPAY_INTENT_URI_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
            "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{payCode}%3F_s" +
            "%3Dweb-other&_t=1472443966571#Intent;" +
            "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";
    private static final String APLPAY_PAYCODE = "aex00155nkzbj7tuxj3vw38";

    /**
     * WeChat Pay
     */
    private static final String WECHATPAY_QRADDRESS = "https://raw.githubusercontent.com/x-falcon/tools/master/w.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        
        CardView cv_alipay = findViewById(R.id.cv_alipay);
        CardView cv_wexin = findViewById(R.id.cv_wexin);
        CardView cv_paypal = findViewById(R.id.cv_paypal);
        CardView cv_bit = findViewById(R.id.cv_bit);
        // Google Pay 相关视图已移除
        CardView cv_google = findViewById(R.id.cv_google);
        CardView cv_google2 = findViewById(R.id.cv_google2);
        CardView cv_google4 = findViewById(R.id.cv_google4);
        CardView cv_google6 = findViewById(R.id.cv_google6);
        CardView cv_google8 = findViewById(R.id.cv_google8);
        CardView cv_google10 = findViewById(R.id.cv_google10);

        // 隐藏 Google Pay 卡片（支付库已移除）
        // IS_GooglePlay 始终为 false，所以显示国内支付方式
        if (IS_GooglePlay) {
            // Google Play 模式下不使用此 Activity
            cv_alipay.setVisibility(View.GONE);
            cv_wexin.setVisibility(View.GONE);
            cv_paypal.setVisibility(View.GONE);
            cv_bit.setVisibility(View.GONE);
        } else {
            cv_google.setVisibility(View.GONE);
            cv_google2.setVisibility(View.GONE);
            cv_google4.setVisibility(View.GONE);
            cv_google6.setVisibility(View.GONE);
            cv_google8.setVisibility(View.GONE);
            cv_google10.setVisibility(View.GONE);
        }

        final Button button_alipay = findViewById(R.id.bt_alipay);
        final Button button_wechat = findViewById(R.id.bt_weixin);
        final Button button_paypal = findViewById(R.id.bt_paypal);
        final Button button_bit = findViewById(R.id.bt_bit);

        button_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donatePayPalOnClick(v);
            }
        });

        button_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateAlipayOnclick(v);
            }
        });

        button_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateWechatOnclick(v);
            }
        });

        button_bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateBitcoinOnClick(v);
            }
        });
        button_bit.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return donateBitcoinOnLongClick(v);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void donateAlipayOnclick(View view) {
        try {
            startActivity(Intent.parseUri(APLPAY_INTENT_URI_FORMAT.replace("{payCode}", APLPAY_PAYCODE), Intent.URI_INTENT_SCHEME));
        } catch (Exception e) {
            openBrowser(APLPAY_QRADDRESS);
        }
    }

    public void donatePayPalOnClick(View view) {
        try {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PAYPAL_ADDRESS));
            startActivity(viewIntent);
        } catch (ActivityNotFoundException e) {
            LogUtils.d(TAG, e.toString(), e);
        }
    }

    private void donateWechatOnclick(View view) {
        openBrowser(WECHATPAY_QRADDRESS);
    }

    /**
     * Donate with bitcoin by opening a bitcoin: intent if available.
     */
    public void donateBitcoinOnClick(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("bitcoin:" + BITCOIN_ADDRESS)));
        } catch (ActivityNotFoundException e) {
            view.performLongClick();
        }
    }

    public boolean donateBitcoinOnLongClick(View view) {
        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(BITCOIN_ADDRESS, BITCOIN_ADDRESS);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.tip_bitcoin, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void openBrowser(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        } catch (Exception e) {
            LogUtils.d(TAG, "UNKNOW ERROR");
        }
    }
}