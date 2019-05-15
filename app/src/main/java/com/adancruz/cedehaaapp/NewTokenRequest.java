package com.adancruz.cedehaaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewTokenRequest extends StringRequest {

    private Map<String, String> params;

    public NewTokenRequest (String correo, String token, Response.Listener<String> listener) {

        super(Method.POST, DireccionesURL.TOKEN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("correo", correo);
        params.put("token", token);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
