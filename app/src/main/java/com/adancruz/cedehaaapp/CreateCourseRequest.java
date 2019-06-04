package com.adancruz.cedehaaapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateCourseRequest extends StringRequest {

    //private static final String NEW_COURSE_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/new-course.php";
    //private static final String EDIT_COURSE_REQUEST_URL = "https://cedehaa-app.000webhostapp.com/edit-course.php";
    private Map<String, String> params;

    public CreateCourseRequest (Boolean editar, String url, String exTitulo, String exDescBreve,
                                String exFechaInicio, String titulo, String numImagen,
                                String descBreve, String descGeneral, String fechaInicio,
                                String limiteEstudiantes, String estado,
                                Response.Listener<String> listener) {
        super(Request.Method.POST, url, listener, null);
        params = new HashMap<>();
        if (editar) {
            params.put("exTitulo", exTitulo);
            params.put("exDescBreve", exDescBreve);
            params.put("exFechaInicio", exFechaInicio);
        }
        params.put("titulo", titulo);
        params.put("numImagen", numImagen);
        params.put("descBreve", descBreve);
        params.put("descGeneral", descGeneral);
        params.put("fechaInicio", fechaInicio);
        params.put("limiteEstudiantes", limiteEstudiantes);
        params.put("estado", estado);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}