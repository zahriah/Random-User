package my.edu.utem.randomuser;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView nameTextView, emailTextView, phoneTextView, addressTextView, dobTextView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.name_textView);
        emailTextView = findViewById(R.id.email_textView);
        phoneTextView = findViewById(R.id.phone_textView);
        addressTextView = findViewById(R.id.address_textView);
        dobTextView = findViewById(R.id.dob_textView);
        progressBar=findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);

    }

    public void getUser(View view) {
        // Instantiate the RequestQueue.
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://randomuser.me/api";


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        progressBar.setVisibility(View.GONE);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject personObject = jsonObject.getJSONArray("results").getJSONObject(0);

                            String imageURL = personObject.getJSONObject("picture").getString("thumbnail");
                            Glide.with(MainActivity.this).load(imageURL).into(imageView);

                            String title = personObject.getJSONObject("name").getString("title");
                            String firstName = personObject.getJSONObject("name").getString("first");
                            String lastName = personObject.getJSONObject("name").getString("last");
                            nameTextView.setText(title + " " +firstName+" "+lastName);

                            emailTextView.setText(personObject.getString("email"));

                            phoneTextView.setText(personObject.getString("phone"));

                            String street = personObject.getJSONObject("location").getString("street");
                            String postcode = personObject.getJSONObject("location").getString("postcode");
                            String city = personObject.getJSONObject("location").getString("city");
                            String state = personObject.getJSONObject("location").getString("state");
                            addressTextView.setText(street + ", " +postcode+" "+city+", "+state);

                            dobTextView.setText(personObject.getJSONObject("dob").getString("date"));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                nameTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
