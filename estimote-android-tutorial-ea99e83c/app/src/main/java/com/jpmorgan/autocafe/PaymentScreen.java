package com.jpmorgan.autocafe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by tapas on 6/22/2017.
 */

public class PaymentScreen extends AppCompatActivity {
    private Button payTMButton , sodexoButton, cashButton;
    private TextView totalPriceText, vendorName;
    private String vendorNameString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);

        payTMButton = (Button) findViewById(R.id.payTMButton);
        sodexoButton = (Button) findViewById(R.id.sodexoButton);
        cashButton = (Button) findViewById(R.id.cashButton);

        totalPriceText = (TextView) findViewById(R.id.totalPrice);
        vendorName = (TextView) findViewById(R.id.vendorName);

        Intent intent = getIntent();
        final int totalPrice = intent.getIntExtra("totalPrice", 0);
        vendorNameString = intent.getStringExtra("vendorName");

        if (TextUtils.isEmpty(vendorNameString)) {

        } else {
            vendorName.append(vendorNameString);
        }

        totalPriceText.append(" " + totalPrice + " /-");

        payTMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://paytm.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        sodexoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentScreen.this, "Paying "+totalPrice+ "/- using Sodexo !!!", Toast.LENGTH_SHORT).show();
            }
        });

        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentScreen.this, "Paying "+totalPrice+ "/- by cash !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
