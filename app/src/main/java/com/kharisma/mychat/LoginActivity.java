package com.kharisma.mychat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText et_nohp, et_pass;
    Button bt_login, bt_regis;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");

    SharedPreferences mylocaldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_nohp = (EditText) findViewById(R.id.et_nohp);
        // et_pass = (EditText ) findViewById(R.id.et_pass);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_regis = (Button) findViewById(R.id.bt_regis);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomorhp = et_nohp.getText().toString();
                //String password = et_pass.getText().toString();

                //pemanggilan data pada firebase berdasarkan no hp

                userRef.child(nomorhp).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mylocaldata = getSharedPreferences("mylocaldata", MODE_PRIVATE);
                        User user = new User();
                        if (dataSnapshot.exists()) {
                            user.setNama(dataSnapshot.child("nama").getValue(String.class));
                            user.setEmail(dataSnapshot.child("email").getValue(String.class));
                            user.setTelepon(dataSnapshot.child("telepon").getValue(String.class));

                            SharedPreferences.Editor editor = mylocaldata.edit();
                            editor.putString("uid", user.getTelepon());
                            editor.apply();

                            //pindah ke main activity

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = "User tidak ditemukan";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        bt_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}

