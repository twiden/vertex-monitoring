package com.twiden.vertxmonitoring;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayServicesActivity extends AppCompatActivity {

    private GoogleApiClient client;
    private String server;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_service_activity);
        Intent intent = getIntent();
        server = intent.getStringExtra(MainActivity.CONNECT_STR);
        queue = Volley.newRequestQueue(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        refreshServices();
    }

    public void deleteService(View view) {
        ServiceItemView v = (ServiceItemView) view.getParent().getParent();
        String url = server + "/service/" + v.getID();

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        refreshServices();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("!!!!! DELTE SERVICE ERROR"); // TODO error handling here
            }
        });

        queue.add(stringRequest);
    }

    public void refreshServices(View view) {
        refreshServices();
    }

    public void refreshServices() {
        final DisplayServicesActivity me = this;
        String url = server + "/service";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray arr = response.getJSONArray("services");
                        ViewGroup layout = (ViewGroup) findViewById(R.id.service_list);
                        layout.removeAllViews();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            ServiceItemView view = new ServiceItemView(me);
                            view.setName(o.getString("name"));
                            view.setStatus(o.getString("status"));
                            view.setID(o.getString("id"));
                            view.setUrl(o.getString("url"));
                            view.setLastCheck(o.getString("lastCheck"));
                            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            layout.addView(view);
                        }
                    } catch (JSONException e) {
                        System.out.println("!!!!! REFRESH SERVICE ERROR 1"); // TODO error handling here
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("!!!!! REFRESH SERVICE ERROR 2"); // TODO error handling here

                }
            });

        queue.add(jsObjRequest);
    }

    public void addService(View view) {
        String name = ((EditText) findViewById(R.id.edit_service_name)).getText().toString();
        String addr = ((EditText) findViewById(R.id.edit_service_adress)).getText().toString();

        final String jsonBody = "{\"name\":\"" + name + "\", \"url\":\"" + addr + "\"}";
        final String url = server + "/service";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    refreshServices();
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("!!!!! ADD SERVICE ERROR 2"); // TODO error handling here
                }
            }
        ) {
            @Override
            public byte[] getBody() {
                return jsonBody.getBytes();
            }
        };

        queue.add(stringRequest);
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DisplayMessage Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
