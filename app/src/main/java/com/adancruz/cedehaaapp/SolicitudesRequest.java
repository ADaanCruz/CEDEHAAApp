package com.adancruz.cedehaaapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SolicitudesRequest extends StringRequest {

    private static final String SOLICITUDES_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/solicitudes.php";
    private Map<String, String> params;

    public SolicitudesRequest(  String boton, String titulo, String fecha, String correo,
                                Response.Listener<String> listener) {
        super(Method.POST, SOLICITUDES_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("boton", boton);
        params.put("titulo", titulo);
        params.put("fecha", fecha);
        params.put("correo", correo);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
