package com.adancruz.cedehaaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CourseRequest extends JsonObjectRequest {

    private static final String COURSE_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/all-courses.php";
    private Map<String, String> params;

    public CourseRequest(Response.Listener<JSONObject> listener) {
        super(COURSE_REQUEST_URL, null, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
