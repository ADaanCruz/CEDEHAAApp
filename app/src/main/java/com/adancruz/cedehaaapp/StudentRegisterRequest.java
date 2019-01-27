package com.adancruz.cedehaaapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class StudentRegisterRequest extends StringRequest {

    private static final String STUDENT_REGISTER_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/student-register.php";
    private Map<String, String> params;

    public StudentRegisterRequest (String nombre, String apellidoPaterno, String apellidoMaterno,
                                   String correo, String contrasena, String telefono, String tipoDeUsuario,
                                   String fotoDePerfil, Response.Listener<String> listener) {

        super(Request.Method.POST, STUDENT_REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("nombre", nombre);
        params.put("apellidoPaterno", apellidoPaterno);
        params.put("apellidoMaterno", apellidoMaterno);
        params.put("correo", correo);
        params.put("contrasena", contrasena);
        params.put("telefono", telefono);
        params.put("tipoDeUsuario", tipoDeUsuario);
        params.put("fotoDePerfil", fotoDePerfil);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
