package com.login_signup_screendesign_demo;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.login_signup_screendesign_demo.Mode.User;

public class Main2Activity extends Activity {
    public static TextView textView;
    public static EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        editText = findViewById(R.id.limit);
                setContentView(R.layout.activity_main2);
        textView = findViewById(R.id.cloudWater);
        // forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();

        DatabaseReference tableuser2 = database2.getReference("User/0001");
        DatabaseReference tableuser3=database2.getReference("User/0001/limit");
        final DatabaseReference myRef = database2.getReference("User/0001/tap");
        tableuser2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                String value = String.valueOf(user.getWater() + " ml");
                textView.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        simpleSwitch.setTextOff("off");
        simpleSwitch.setTextOn("On");
        Boolean switchState = simpleSwitch.isChecked();
        String tap = "";
/*String getValue = textView.getText().toString();
String getLimit = editText.getText().toString();
tableuser3.setValue(getLimit);

if(getValue>=getLimit){
    myRef.setValue("CLOSE");
    simpleSwitch.setTextOff("off");
    Toast.makeText(Main2Activity.this, "Tap is Closed!", Toast.LENGTH_SHORT).show();
}*/

       simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   myRef.setValue("OPEN");
                   Toast.makeText(Main2Activity.this, "Tap is Open!", Toast.LENGTH_SHORT).show();

               }else{
                   myRef.setValue("CLOSE");
                   Toast.makeText(Main2Activity.this, "Tap is Closed!", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }


}
