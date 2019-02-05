package com.login_signup_screendesign_demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {
    private Button TouchMe;
    private EditText LastMonthWaterBill;
    private TextView LastMonthWaterBillCalculation;
    private TextView ExpectedWaterBillCalculation;
    private EditText ExpectedWaterBill;
    private TextView ServiceCharge;
    private TextView RealAmountt;
    private Button GetToNextPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        TouchMe = (Button) findViewById(R.id.TouchME);
GetToNextPage = (Button)findViewById(R.id.GetToNextPage);
ServiceCharge = (TextView)findViewById(R.id.Servicecharge);
RealAmountt = (TextView)findViewById(R.id.REalAmount);
        LastMonthWaterBill = (EditText) findViewById(R.id.LastMonthWaterBill);
        ExpectedWaterBill = (EditText) findViewById(R.id.ExpectedWaterBill);


        LastMonthWaterBillCalculation = (TextView) findViewById(R.id.LastMonthWaterBillCalculation);
        ExpectedWaterBillCalculation = (TextView) findViewById(R.id.ExpectedWaterBillCalculation);


        TouchMe.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onClick(View v) {
                String getLastMonthWaterBill = LastMonthWaterBill.getText().toString();
                String getExpectedWaterBill = ExpectedWaterBill.getText().toString();
                double ExpectedMonth = Integer.valueOf(getExpectedWaterBill);

                double tax = ((ExpectedMonth)*12)/100;
                double totalwithouttax = ExpectedMonth -tax;
                double realamount;
                double units;
                double serviceCharge;
                double RealAmount;

                if (totalwithouttax <= 110) {
                    serviceCharge = 50;
                    realamount = totalwithouttax - 50;
                    units = realamount / 12;


                } else if (totalwithouttax <= 205) {
                    serviceCharge = 65;
                    realamount = totalwithouttax - 65;
                    units = ((realamount - 60) / 16) + 5;

                } else if (totalwithouttax <= 310) {
                    serviceCharge = 70;
                    realamount = totalwithouttax - 70;
                    units = ((realamount - 140) / 20) + 10;
                } else if (totalwithouttax <= 520) {
                    serviceCharge = 80;
                    realamount = totalwithouttax - 80;
                    units = ((realamount - 240) / 40) + 15;
                } else if (totalwithouttax <= 830) {
                    serviceCharge = 100;
                    realamount = totalwithouttax - 100;
                    units = ((realamount - 440) / 58) + 20;
                } else if (totalwithouttax <= 1370) {
                    serviceCharge = 200;
                    realamount = totalwithouttax - 200;
                    units = ((realamount - 730) / 88) + 25;
                } else if (totalwithouttax <= 2095) {
                    serviceCharge = 400;
                    realamount = totalwithouttax - 400;
                    units = ((realamount - 1170) / 105) + 30;
                } else if (totalwithouttax <= 2945) {
                    serviceCharge = 650;
                    realamount = totalwithouttax - 650;
                    units = ((realamount - 1695) / 120) + 40;
                } else if (totalwithouttax <= 3945) {
                    serviceCharge = 1000;
                    realamount = totalwithouttax - 1000;
                    units = ((realamount - 2295) / 130) + 50;
                } else {
                    serviceCharge = 1600;
                    realamount = totalwithouttax - 1600;
                    units = ((realamount - 2945) / 140) + 75;
                }



                ServiceCharge.setText("Service Charge : Rs " +serviceCharge );
                ExpectedWaterBillCalculation.setText("Expected Units to be used :" + String.valueOf(String.format("%.2f",units) ) + " Units");
                LastMonthWaterBillCalculation.setText("Tax Applied : Rs " + tax );
                RealAmountt.setText("Amount Excl. S.Charge & Tax : Rs " + String.format("%.1f", realamount ));
               //TouchMe.setText("Touch me Again");


                        TouchMe.setText("Touch me Again");
                        GetToNextPage.setVisibility(View.VISIBLE);
                        GetToNextPage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent newActivity = new Intent(Main3Activity.this,Main2Activity.class);
                                startActivity(newActivity);
                            }
                        });
                     //   Intent newActivity = new Intent(Main3Activity.this,MainActivity.class);
                       // startActivity(newActivity);



            }
        });


    }
}
