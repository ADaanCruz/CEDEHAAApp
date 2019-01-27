package com.adancruz.cedehaaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CourseRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/all-courses.php";
    private Map<String, String> params;

    public CourseRequest (Response.Listener<String> listener) {

        super(Method.GET, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
