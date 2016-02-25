package com.example.mizuno.prog_202;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button b1 =(Button) findViewById(R.id.button1);
        Button b2 =(Button) findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                int rowid = extras.getInt("ID");
                EditText et1 = (EditText) findViewById(R.id.EditText1);
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("created", Calendar.getInstance().getTime().toString());
                values.put("comment", et1.getText().toString());
                long rowID = db.update("bbs", values, "id=" + rowid, null);
                if (rowID == -1) {
                    db.close();
                    //throw new SQLException("Failed to update row");
                }
                db.close();
                TextView tv = (TextView) findViewById(R.id.TextView01);
                tv.setBackgroundColor(Color.BLUE);
                tv.setText("Dataを登録しました。");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO 自動生成されたメソッド・スタブ
                finish();
                Intent intent = new Intent(EditActivity.this,Prog202Activity.class);
                startActivity(intent);
            }
        });

    }

}
