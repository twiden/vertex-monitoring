package com.twiden.backend;

import com.twiden.backend.Service;
import com.twiden.backend.ServiceNotFound;
import com.twiden.backend.StorageIOException;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.UUID;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Racy storage. Concurrent requests might break this.
// Linear seeking in json file is not very efficient but ok for this implementation.
public class Storage {

    static String db = "database.json";
    static String emptyDb = "{\"services\": []}";

    Storage() throws StorageIOException {
        this.ensureDatabaseIsInitialized();
    }

    public ArrayList<Service> listServices() throws StorageIOException {
        JSONParser parser = new JSONParser();
        JSONArray db_services;

        try {
            JSONObject jsonObject =  (JSONObject) parser.parse(new FileReader(Storage.db));
            db_services = (JSONArray) jsonObject.get("services");
        } catch (IOException e) {
            throw new StorageIOException("Database file does not exist or could not be created");
        } catch (ParseException e) {
            throw new StorageIOException("Database file does not contain valid json");
        }

        ArrayList<Service> service_instances = new ArrayList<>();
        Iterator<JSONObject> iterator = db_services.iterator();

        while (iterator.hasNext()) {
            service_instances.add(new Service(iterator.next()));
        }

        return service_instances;
    }

    public String createService(String name, String url) throws StorageIOException {
        String id = UUID.randomUUID().toString();
        ArrayList<Service> services = listServices();
        services.add(new Service(id, name, "", url, ""));
        writeServices(services);
        return id;
    }

    public void deleteService(String id) throws StorageIOException, ServiceNotFound {
        ArrayList<Service> services = listServices();
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId().equals(id)) {
                services.remove(i);
                writeServices(services);
                return;
            }
        }
        throw new ServiceNotFound(id);
    }

    public void setStatus(String id, String status, String timestamp) throws ServiceNotFound, StorageIOException {
        ArrayList<Service> services = listServices();
        for (int i = 0; i < services.size(); i++) {
            Service s = services.get(i);
            if (s.getId().equals(id)) {
                s.setStatus(status);
                s.setLastCheck(timestamp);
                writeServices(services);
                return;
            }
        }
        throw new ServiceNotFound(id);
    }

    private void writeServices(ArrayList<Service> services) throws StorageIOException {
        JSONArray service_list = new JSONArray();
        for (Service service : services) {
            service_list.add(service.toJSONObject());
        }
        JSONObject obj = new JSONObject();
        obj.put("services", service_list);

        try {
            FileWriter file = new FileWriter(Storage.db);
            file.write(obj.toJSONString());
            file.flush();
            file.close();
        }
        catch (IOException e) {
            throw new StorageIOException("Could not write services to database: " + e.toString());
        }
    }

    private void ensureDatabaseIsInitialized() throws StorageIOException {
        boolean db_exists = true;
        try {
            new FileReader(Storage.db);
        } catch (IOException e) {
            db_exists = false;
        }
        if (!db_exists) {
            try {
                FileWriter oFile = new FileWriter(Storage.db, false);
                oFile.write(Storage.emptyDb);
                oFile.close();
            }
            catch (IOException e) {
                throw new StorageIOException("Could not ensure database exists: " + e.toString());
            }
        }
    }

    public void clearDatabase() {
        new File(Storage.db).delete();
    }
}
