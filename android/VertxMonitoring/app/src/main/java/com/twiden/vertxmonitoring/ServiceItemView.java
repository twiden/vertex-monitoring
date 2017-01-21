package com.twiden.vertxmonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceItemView extends LinearLayout
{
    private TextView txtName;
    private String txtId;
    private TextView txtStatus;
    private TextView txtLastCheck;

    public ServiceItemView(Context context)
    {
        super(context);
        initControl(context);
    }

    public void setName(String name) { txtName.setText(name); }
    public void setStatus(String status) { txtStatus.setText(status); }
    public void setID(String id) { this.txtId = id; }
    public void setLastCheck(String lastCheck) { txtLastCheck.setText(lastCheck); }

    private void initControl(Context context)
    {
        LayoutInflater inflater;
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.service_item, this);
        txtName = (TextView)findViewById(R.id.name);
        txtStatus = (TextView)findViewById(R.id.status);
        txtLastCheck = (TextView)findViewById(R.id.last_check);

    }
}