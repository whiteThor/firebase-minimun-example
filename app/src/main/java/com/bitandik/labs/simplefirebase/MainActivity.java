package com.bitandik.labs.simplefirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  private FirebaseDatabase database;

  private String MESSAGE_CHILD = "message";
  private String WORDS_CHILD = "words";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      database = FirebaseDatabase.getInstance();

      DatabaseReference messageReference = database.getReference().child(MESSAGE_CHILD);
      messageReference.setValue("Hello, World!");

      messageReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            if (snapshot.getValue() != null) {
              String value = snapshot.getValue(String.class);
              showMessage(value);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            Log.w(TAG, "Failed to read value.", error.toException());
        }
      });

      DatabaseReference wordsReference = database.getReference().child(WORDS_CHILD);
      wordsReference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
          showMessage(dataSnapshot.getValue().toString());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {
          Log.w(TAG, "Failed to read value.", databaseError.toException());
        }
      });


      final Button btn = (Button)findViewById(R.id.button);
      final EditText editText = (EditText)findViewById(R.id.editText);

      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            DatabaseReference newElementReference = database.getReference().child(WORDS_CHILD).push();
            newElementReference.setValue(editText.getText().toString());
            editText.setText("");
          }
      });

  }

  public void showMessage(String msg) {
    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    Log.d(TAG, "Value is: " + msg);
  }
}
