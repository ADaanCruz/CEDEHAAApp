package com.adancruz.cedehaaapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EmailRequest extends StringRequest {
    private Map<String, String> params;

    public EmailRequest (String to, String tipo, String random, Response.Listener<String> listener) {
        super(Request.Method.POST, DireccionesURL.EMAIL_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("to", to);
        params.put("tipo", tipo);
        params.put("random", random);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
