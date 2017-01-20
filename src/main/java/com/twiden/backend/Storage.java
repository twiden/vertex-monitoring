package com.twiden.backend;

import java.io.FileReader;
import java.util.Iterator;
import java.util.ArrayList;
import com.twiden.backend.Service;
import java.io.IOException;

import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


// Racy storage. Simultaneous requests might break this.
public class Storage {

    static String db = "database.json";
    static String emptyDb = "{\"services\": []}";

    Storage() throws IOException{
        this.ensureDatabaseIsInitialized();
    }

    public ArrayList<Service> listServices() throws IOException, ParseException{
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(Storage.db));
        JSONObject jsonObject =  (JSONObject) obj;
        JSONArray db_services = (JSONArray) jsonObject.get("services");
        ArrayList<Service> service_instances = new ArrayList<>();
        Iterator<JSONObject> iterator = db_services.iterator();

        while (iterator.hasNext()) {
            JSONObject service = iterator.next();
            service_instances.add(new Service(
                (String) service.get("id"),
                (String) service.get("name"),
                (String) service.get("status"),
                (String) service.get("url"),
                (String) service.get("lastCheck")
            ));
        }

        return service_instances;
    }


    private void ensureDatabaseIsInitialized() throws IOException {
        boolean db_exists = true;
        try {
            new FileReader(Storage.db);
        } catch (IOException e) {
            db_exists = false;
        }
        if (!db_exists) {
            FileWriter oFile = new FileWriter(Storage.db, false);
            oFile.write("{\"services\": []}");
            oFile.close();
        }
    }
}
