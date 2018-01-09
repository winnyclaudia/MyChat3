package com.kharisma.mychat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference("chats");

    ArrayList<Chat> chats = new ArrayList<>();
    EditText et_ketik;
    Button bt_send;

    RecyclerView rv_chat;
    ChatListAdapter adapter;

    User user;
    SharedPreferences mylocaldata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mylocaldata = getSharedPreferences("mylocaldata", MODE_PRIVATE);

        user = getIntent().getParcelableExtra("user");

        if (user==null){
            Intent intent= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        //pembacaan data dari firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Chat chat = postSnapshot.getValue(Chat.class);
                    chats.add(chat);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //pengaktifan recycle view menggunakan chatlistadapter
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        rv_chat.setHasFixedSize(true);
        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatListAdapter(this,chats);
        rv_chat.setAdapter(adapter);

        et_ketik = (EditText) findViewById(R.id.et_ketik);
        bt_send = (Button) findViewById(R.id.bt_send);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat= new Chat();
                chat.setPesan(et_ketik.getText().toString());
                chat.setTanggal(new Date().getTime());
                chat.setSender(user);

                et_ketik.setText(" ");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuUser:
                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuLogout:
                finish();
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }
}
