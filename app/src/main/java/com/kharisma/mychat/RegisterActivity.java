package com.kharisma.mychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    User user;
    EditText et_nama, et_nohpregis, et_email;
    Button bt_regis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_nama = (EditText) findViewById(R.id.et_nama);
        et_nohpregis = (EditText) findViewById(R.id.et_nohpregis);
        et_email = (EditText) findViewById(R.id.et_email);
        bt_regis = (Button) findViewById(R.id.bt_regis);

        bt_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                user.setNama(et_nama.getText().toString());
                user.setTelepon(et_nohpregis.getText().toString());
                user.setEmail(et_email.getText().toString());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRef = database.getReference("users");

                userRef.child(user.getTelepon()).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
