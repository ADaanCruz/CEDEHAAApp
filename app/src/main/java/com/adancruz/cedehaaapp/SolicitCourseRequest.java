package com.adancruz.cedehaaapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SolicitCourseRequest extends StringRequest {
    private static final String SOLICIT_COURSE_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/solicit-course.php";
    private Map<String, String> params;

    public SolicitCourseRequest(String titulo, String fechaInicio, String correo, String verificar,
                                Response.Listener<String> listener) {
        super(Request.Method.POST, SOLICIT_COURSE_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("titulo", titulo);
        params.put("fechaInicio", fechaInicio);
        params.put("correo", correo);
        params.put("verificar", verificar);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
