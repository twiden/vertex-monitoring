package com.twiden.backend;

import com.twiden.backend.Service;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.UUID;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


// Racy storage. Simultaneous requests might break this.
// Throws all sorts of exceptions because if storage breaks there is no point trying to keep the application alive, hard failures are better here.
public class Storage {

    static String db = "database.json";
    static String emptyDb = "{\"services\": []}";

    Storage() throws IOException{
        this.ensureDatabaseIsInitialized();
    }

    public ArrayList<Service> listServices() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(Storage.db));
        JSONObject jsonObject =  (JSONObject) obj;
        JSONArray db_services = (JSONArray) jsonObject.get("services");
        ArrayList<Service> service_instances = new ArrayList<>();
        Iterator<JSONObject> iterator = db_services.iterator();

        while (iterator.hasNext()) {
            service_instances.add(new Service(iterator.next()));
        }

        return service_instances;
    }

    public String createSevice(String name, String url) throws IOException, ParseException {
        String id = UUID.randomUUID().toString();
        ArrayList<Service> services = listServices();
        services.add(new Service(id, name, "", url, ""));
        writeServices(services);
        return id;
    }

    private void writeServices(ArrayList<Service> services) throws IOException {
        JSONArray service_list = new JSONArray();
        for (Service service : services) {
            service_list.add(service.toJSONObject());
        }
        JSONObject obj = new JSONObject();
        obj.put("services", service_list);

        FileWriter file = new FileWriter(Storage.db);
        file.write(obj.toJSONString());
        file.flush();
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
            oFile.write(Storage.emptyDb);
            oFile.close();
        }
    }
}
