package com.example.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class AddNewStudentFormActivity extends AppCompatActivity {
    private static final String TAG = "AddNewStudentFormActivi";
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student_form);
        apiService = new ApiService(this, TAG);

        Toolbar toolbar= findViewById(R.id.toolbar_addNewStudent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);


        final EditText firstNameEt = findViewById(R.id.et_addNewStudent_firstName);
        final EditText lastNameEt = findViewById(R.id.et_addNewStudent_lastName);
        final EditText courseEt = findViewById(R.id.et_addNewStudent_course);
        final EditText scoreEt = findViewById(R.id.et_addNewStudent_score);

        View saveBtn = findViewById(R.id.fab_addNewStudent_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstNameEt.length() > 0 && lastNameEt.length() > 0 && courseEt.length() > 0 && scoreEt.length() > 0){
                    apiService.saveStudent(firstNameEt.getText().toString(), lastNameEt.getText().toString(), courseEt.getText().toString(), Integer.parseInt(scoreEt.getText().toString()), new ApiService.SaveStudentCallback() {
                        @Override
                        public void onSuccess(Student student) {
                            Intent intent = new Intent();
                            intent.putExtra("student", student);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(AddNewStudentFormActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}