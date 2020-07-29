package com.example.students;

import android.content.Context;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String TAG = "ApiService";
    private String url = "http://expertdevelopers.ir/api/v1/experts/student";
    private RetrofitApiService apiService;

    public ApiService(Context context, String requestTag){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request oldRequest = chain.request();
                        Request.Builder newRequestBuilder = oldRequest.newBuilder();
                        newRequestBuilder.addHeader("Accept", "application/json");
                        // newRequestBuilder.addHeader("Authorization", "Bearer dhflksdfkldslfk");

                        return chain.proceed(newRequestBuilder.build());
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://expertdevelopers.ir/api/v1/")
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(RetrofitApiService.class);

    }

    public void getStudentList(final GetStudentListCallback callback){
        apiService.getStudents().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });

    }

    public void saveStudent(String firstName, String lastName, String course, int score, final SaveStudentCallback callback){
        JsonObject studentJsonObject = new JsonObject();
        studentJsonObject.addProperty("first_name", firstName);
        studentJsonObject.addProperty("last_name", lastName);
        studentJsonObject.addProperty("course", course);
        studentJsonObject.addProperty("score", score);

        apiService.saveStudent(studentJsonObject).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });




    }

    public void cancel(){

    }


    public interface SaveStudentCallback {
        public void onSuccess(Student student);
        public void onError(Exception error);
    }

    public interface  GetStudentListCallback {
        public void onSuccess(List<Student> students);
        public void onError(Exception error);
    }
}
