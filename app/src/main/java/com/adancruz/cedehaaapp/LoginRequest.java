package com.adancruz.cedehaaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

  private Map<String, String> params;

  public LoginRequest (String correo, String contrasena, Response.Listener<String> listener) {

    super(Method.POST, DireccionesURL.LOGIN_REQUEST_URL, listener, null);
    params = new HashMap<>();
    params.put("correo", correo);
    params.put("contrasena", contrasena);

  }

  @Override
  public Map<String, String> getParams() {
    return params;
  }

}