package com.adancruz.cedehaaapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteCourseRequest extends StringRequest {

    private Map<String, String> params;

    public DeleteCourseRequest(String titulo, String fechaInicio,
                               Response.Listener<String> listener) {
        super(Request.Method.POST, DireccionesURL.DELETE_COURSE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("titulo", titulo);
        params.put("fechaInicio", fechaInicio);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
