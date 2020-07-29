package com.example.students;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int STUDENTS_REQUEST_CODE = 1001;
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = new ApiService(this, TAG);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        View addNewStudentBtn = findViewById(R.id.fab_main_addNewStudent);
        addNewStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddNewStudentFormActivity.class),STUDENTS_REQUEST_CODE);

            }
        });


        apiService.getStudentList(new ApiService.GetStudentListCallback() {
            @Override
            public void onSuccess(List<Student> students) {
                recyclerView = findViewById(R.id.rv_main_students);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
                studentAdapter = new StudentAdapter(students);
                recyclerView.setAdapter(studentAdapter);
            }

            @Override
            public void onError(Exception error) {
                Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STUDENTS_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Student student = data.getParcelableExtra("student");
            studentAdapter.addNewStudent(student);
            recyclerView.smoothScrollToPosition(0);

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        apiService.cancel();
    }
}