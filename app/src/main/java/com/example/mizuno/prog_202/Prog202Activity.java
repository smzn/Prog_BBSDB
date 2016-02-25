package com.example.mizuno.prog_202;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class Prog202Activity extends AppCompatActivity {

    static SQLiteDatabase mydb;
    Integer[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prog202);

        Button button = (Button)this.findViewById(R.id.button);
        view();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                add();
                view();
            }
        });
    }

    private void add(){
        EditText editText = (EditText)findViewById(R.id.editText);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        String created = Calendar.getInstance().getTime().toString();
        String comment = editText.getText().toString();
        ContentValues values = new ContentValues();
        values.put("created", created);
        values.put("comment", comment);
        //Insert
        long rowID = db.insert("bbs", "", values);
        editText.setText("");
        if (rowID==-1){
            db.close();
            //throw new SQLException("Failed to insert row");
        }db.close();
    }

    private void view(){
        DatabaseHelper hlpr = new DatabaseHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();
        ListView listView = (ListView) findViewById(R.id.listView);
        try{
            Cursor cr = mydb.rawQuery("Select * From bbs Order By id desc", null);
            cr.moveToFirst();
            if(cr.getCount()>0){
                data = new Integer[cr.getCount()];//ID array
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this,android.R.layout.simple_list_item_1);
                for(int cnt =0;cnt<cr.getCount();cnt++){
                    data[cnt]=cr.getInt(0);
                    adapter.add("ID:"+cr.getString(0)+",\ncreated:"+cr.getString(1)+",\ncomment:"+cr.getString(2));
                    cr.moveToNext();
                    listView.setAdapter(adapter);
                }
            }else listView.setAdapter(null);
        }finally{
            mydb.close();

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    delete(data[(int) id]);
                    return false;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO 自動生成されたメソッド・スタブ
                    finish();
                    Intent intent = new Intent(Prog202Activity.this, EditActivity.class);
                    intent.putExtra("ID", data[(int) id]);
                    startActivity(intent);
                }
            });

        }
    }

    private void delete(int id){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        final Toast toast_s = Toast.makeText(this,"削除成功ID = "+id,Toast.LENGTH_LONG);
        final Toast toast_f = Toast.makeText(this,"削除失敗ID = "+id,Toast.LENGTH_LONG);
        int ret;
        try{
            ret = db.delete("bbs","id ="+ id, null);
            view();
        }finally{
            db.close();
        }
        if(ret == 1) toast_s.show();
        else toast_f.show();
    }


}
