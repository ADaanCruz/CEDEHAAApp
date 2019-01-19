package com.adancruz.cedehaaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/login.php";
    private Map<String, String> params;

    public LoginRequest (String correo, String contrasena, Response.Listener<String> listener) {

        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("correo", correo);
        params.put("contrasena", contrasena);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}