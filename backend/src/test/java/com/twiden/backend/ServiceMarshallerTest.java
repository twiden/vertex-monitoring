package com.twiden.backend;

import junit.framework.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class ServiceMarshallerTest extends TestCase {
   protected ServiceMarshaller marshaller;
   private static String sample_json;
   private static Service sample_service_1;
   private static Service sample_service_2;

   protected void setUp(){
      sample_json =  sample_json = "[{\"lastCheck\":\"first lastCheck\",\"name\":\"first name\",\"id\":\"first id\",\"url\":\"first url\",\"status\":\"first status\"},{\"lastCheck\":\"second lastCheck\",\"name\":\"second name\",\"id\":\"second id\",\"url\":\"second url\",\"status\":\"second status\"}]";
      sample_service_1 = new Service("first id", "first name", "first status", "first url", "first lastCheck");
      sample_service_2 = new Service("second id", "second name", "second status", "second url", "second lastCheck");
      marshaller = new ServiceMarshaller();
   }

   public void testConvertingListOfServicesToJSON() {
      ArrayList<Service> services = new ArrayList<>();
      services.add(sample_service_1);
      services.add(sample_service_2);
      JSONArray json = marshaller.servicesToJSON(services);
      assertEquals(sample_json, json.toString());
   }

   public void testConvertingEmptyListToJSON() {
      ArrayList<Service> services = new ArrayList<>();
      JSONArray json = marshaller.servicesToJSON(services);

      String expected = "[]";
      assertEquals(expected, json.toString());
   }

   public void testConvertingJSONArrayToListOfServices() {
      JSONArray json =  (JSONArray) new JSONTokener(sample_json).nextValue();
      ArrayList<Service> services = marshaller.servicesFromJSON(json);
      assertEquals(2, services.size());
      assertEquals(services.get(0), sample_service_1);
      assertEquals(services.get(1), sample_service_2);
   }

   public void testConvertingEmptyJSONArrayToServices() {
      ArrayList<Service> services = marshaller.servicesFromJSON(new JSONArray());
      assertEquals(0, services.size());
   }
}

