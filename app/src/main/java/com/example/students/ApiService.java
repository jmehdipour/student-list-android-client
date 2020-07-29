package com.example.students;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiService {
    public static RequestQueue requestQueue;
    private static final String TAG = "ApiService";
    private String requestTag;
    private Gson gson;
    private String url = "http://expertdevelopers.ir/api/v1/experts/student";

    public ApiService(Context context, String requestTag){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        this.requestTag = requestTag;
        gson = new Gson();

    }

    public void getStudentList(final GetStudentListCallback callback){
        GsonRequest<List<Student>> gsonRequest = new GsonRequest<>(Request.Method.GET, url, new TypeToken<List<Student>>() {
        }.getType(), new Response.Listener<List<Student>>() {
            @Override
            public void onResponse(List<Student> response) {
                callback.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });

        gsonRequest.setTag(requestTag);
        requestQueue.add(gsonRequest);

    }

    public void saveStudent(String firstName, String lastName, String course, int score, final SaveStudentCallback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name",firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("course", course);
            jsonObject.put("score", score);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GsonRequest<Student> request = new GsonRequest<Student>(Request.Method.POST, url, jsonObject, Student.class, new Response.Listener<Student>() {
            @Override
            public void onResponse(Student response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        request.setTag(requestTag);
        requestQueue.add(request);


    }

    public void cancel(){
        requestQueue.cancelAll(requestTag);
    }


    public interface SaveStudentCallback {
        public void onSuccess(Student student);
        public void onError(VolleyError error);
    }

    public interface  GetStudentListCallback {
        public void onSuccess(List<Student> students);
        public void onError(VolleyError error);
    }
}
