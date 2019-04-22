package com.adancruz.cedehaaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NotificacionesRequest extends StringRequest {

    private static final String NOTIFICATIONS_REQUEST = "http://projects-as-a-developer.online/notifications.php";
    private Map<String, String> params;

    public NotificacionesRequest(String titulo, String mensaje, Response.Listener<String> listener) {
        super(Method.POST, NOTIFICATIONS_REQUEST, listener, null);
        params = new HashMap<>();
        params.put("titulo", titulo);
        params.put("mensaje", mensaje);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
