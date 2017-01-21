package com.twiden.vertxmonitoring;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceItemView extends LinearLayout
{
    private TextView txtName;
    private String txtId;
    private TextView txtStatus;
    private TextView txtUrl;
    private TextView txtLastCheck;

    public ServiceItemView(Context context)
    {
        super(context);
        initControl(context);
    }

    public void setName(String name) { txtName.setText(name); }
    public void setStatus(String status) {
        txtStatus.setText(status);
        if (status.equals("OK")) {
            txtName.setTextColor(Color.parseColor("#4F8A10"));
        } else {
            txtName.setTextColor(Color.parseColor("#D8000C"));
        }
    }
    public void setID(String id) { this.txtId = id; }
    public void setUrl(String url) { this.txtUrl.setText(url); }
    public String getID() { return txtId; }
    public void setLastCheck(String lastCheck) { txtLastCheck.setText(lastCheck); }

    private void initControl(Context context)
    {
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.service_item, this);
        txtName = (TextView)findViewById(R.id.name);
        txtUrl = (TextView)findViewById(R.id.the_url);
        txtStatus = (TextView)findViewById(R.id.status);
        txtLastCheck = (TextView)findViewById(R.id.last_check);
    }

}