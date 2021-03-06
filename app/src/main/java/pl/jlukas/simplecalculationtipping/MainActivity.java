package pl.jlukas.simplecalculationtipping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;

    private EditText accountEditText;
    private TextView accountTextView;

    private TextView precentTextView;
    private SeekBar precentSeekBar;

    private TextView tipTextView;

    private TextView amountTextView;

    private NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
    private NumberFormat precentInstance = NumberFormat.getPercentInstance();

    private double billAmount = 0.0;

    private double tipPrecent = 0.15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Simple Calculation Tipping");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        accountEditText = findViewById(R.id.accountEditText);
        accountTextView = findViewById(R.id.accountTextView);

        precentTextView = findViewById(R.id.precentTextView);
        precentSeekBar = findViewById(R.id.precentSeekBar);

        tipTextView = findViewById(R.id.tipTextView);
        amountTextView = findViewById(R.id.amountTextView);

        accountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    billAmount = Double.parseDouble(s.toString()) / 100.0;
                    accountTextView.setText(currencyInstance.format(billAmount));
                } catch(NumberFormatException ex) {
                    accountTextView.setText("");
                    billAmount = 0.0;
                }

                tipAndTotalAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        precentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipPrecent = progress / 100.0;
                tipAndTotalAmount();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void tipAndTotalAmount() {
        double tipAmount = billAmount * tipPrecent;
        double totalAmount = tipAmount + billAmount;

        precentTextView.setText(precentInstance.format(tipPrecent));
        tipTextView.setText(currencyInstance.format(tipAmount));
        amountTextView.setText(currencyInstance.format(totalAmount));
    }
}
