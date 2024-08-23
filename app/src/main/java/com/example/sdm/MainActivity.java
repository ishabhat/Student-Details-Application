package com.example.sdm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
    EditText Rollno, Name, Marks, MA;
    Button Insert, Delete, Update, View, ViewAll;
    SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rollno = findViewById(R.id.Rollno);
        Name = findViewById(R.id.Name);
        Marks = findViewById(R.id.Marks);
        MA = findViewById(R.id.Marks2);
        Insert = findViewById(R.id.Insert);
        Delete = findViewById(R.id.Delete);
        Update = findViewById(R.id.Update);
        View = findViewById(R.id.View);
        ViewAll = findViewById(R.id.ViewAll);

        Insert.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Update.setOnClickListener(this);
        View.setOnClickListener(this);
        ViewAll.setOnClickListener(this);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student1(rollno VARCHAR, name VARCHAR, marks VARCHAR, marks2 VARCHAR);");
    }

    @Override
    public void onClick(View view) {
        if (view == Insert) {
            if (Rollno.getText().toString().trim().isEmpty() ||
                    Name.getText().toString().trim().isEmpty() ||
                    Marks.getText().toString().trim().isEmpty() ||
                    MA.getText().toString().trim().isEmpty()) {
                showMessage("Error", "Please enter all values");
                return;
            }

            db.execSQL("INSERT INTO student1 VALUES('" + Rollno.getText() + "','" + Name.getText() +
                    "','" + Marks.getText() + "','" + MA.getText() + "');");
            showMessage("Success", "Record added");
            clearText();
        }

        if (view == Delete) {
            if (Rollno.getText().toString().trim().isEmpty()) {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM student1 WHERE rollno='" + Rollno.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM student1 WHERE rollno='" + Rollno.getText() + "'");
                showMessage("Success", "Record Deleted");
            } else {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }

        if (view == Update) {
            if (Rollno.getText().toString().trim().isEmpty()) {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM student1 WHERE rollno='" + Rollno.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("UPDATE student1 SET name='" + Name.getText() + "', marks='" + Marks.getText() + "', marks2='" + MA.getText() +
                        "' WHERE rollno='" + Rollno.getText() + "'");
                showMessage("Success", "Record Modified");
            } else {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }

        if (view == View) {
            if (Rollno.getText().toString().trim().isEmpty()) {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM student1 WHERE rollno='" + Rollno.getText() + "'", null);
            if (c.moveToFirst()) {
                Name.setText(c.getString(1));
                Marks.setText(c.getString(2));
                MA.setText(c.getString(3));
            } else {
                showMessage("Error", "Invalid Rollno");
                clearText();
            }
        }

        if (view == ViewAll) {
            Cursor c = db.rawQuery("SELECT * FROM student1", null);
            if (c.getCount() == 0) {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                int a1 = Integer.parseInt(c.getString(2));
                int b1 = Integer.parseInt(c.getString(3));
                int avg = (a1 + b1) / 2;

                buffer.append("Rollno: ").append(c.getString(0)).append("\n");
                buffer.append("Name: ").append(c.getString(1)).append("\n");
                buffer.append("Marks: ").append(c.getString(2)).append("\n");
                buffer.append("Marks2: ").append(c.getString(3)).append("\n");
                buffer.append("Avg: ").append(avg).append("\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        Rollno.setText("");
        Name.setText("");
        Marks.setText("");
        MA.setText("");
        Rollno.requestFocus();
    }
}