package com.example.medet.demoretrofit.UI.Activities;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medet.demoretrofit.API.Service.API;
import com.example.medet.demoretrofit.API.Service.APIClient;
import com.example.medet.demoretrofit.API.model.GitHub;
import com.example.medet.demoretrofit.R;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

//    private ListView listView;
    private EditText etLogin;
    private EditText etPassword;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        listView = findViewById(R.id.listView);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPass);
        textView = findViewById(R.id.tvRepos);



    }

    public void onClick(View view) {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        String md5_password = md5(password);
        String basicAuth = "Basic "+ Base64.encodeToString(String.format("%s:%s",login,md5_password).
                        getBytes(), Base64.NO_WRAP);

        view.setVisibility(View.GONE);
        etLogin.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);

        API api = APIClient.getClient().create(API.class);
        Call<GitHub> call = api.getGitHubData(basicAuth);

        if (isNetworkConnected()){
            call.enqueue(new Callback<GitHub>() {
                @Override
                public void onResponse(@NonNull Call<GitHub> call, @NonNull Response<GitHub> response) {
                    final GitHub gitHub = response.body();
                    String listOfRepos;
                    if (gitHub.getFollowersUrl() != null){
                        listOfRepos = gitHub.getFollowersUrl();
                    }else {
                        listOfRepos = "NullPointer";
                        Log.d("myTag","NullPointer getFollowingUrl");
                    }
                    textView.setText(listOfRepos);


                }

                @Override
                public void onFailure(Call<GitHub> call, Throwable t) {
                    Log.d("myTag","Request failed");
                }
            });
        }else {
            Toast.makeText(MainActivity.this, "You have to connect your device to internet", Toast.LENGTH_LONG).show();
        }

    }

    public String md5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")), 0, s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}