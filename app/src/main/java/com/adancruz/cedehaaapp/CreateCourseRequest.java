package com.adancruz.cedehaaapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateCourseRequest extends StringRequest {

    private static final String NEW_COURSE_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/new-course.php";
    private Map<String, String> params;

    public CreateCourseRequest (String titulo, String numImagen, String descBreve,
                                String descGeneral, String fechaInicio,
                                String limiteEstudiantes, Response.Listener<String> listener) {

        super(Request.Method.POST, NEW_COURSE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("titulo", titulo);
        params.put("numImagen", numImagen);
        params.put("descBreve", descBreve);
        params.put("descGeneral", descGeneral);
        params.put("fechaInicio", fechaInicio);
        params.put("limiteEstudiantes", limiteEstudiantes);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
