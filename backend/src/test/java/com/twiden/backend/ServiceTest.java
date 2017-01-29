package com.twiden.backend;

import junit.framework.*;


public class ServiceTest extends TestCase {

   public void testHashCode() {
      int first_hash = new Service("A", "B", "C", "D", "E").hashCode();
      assertEquals(first_hash, new Service("A", "B", "C", "D", "E").hashCode());
      assertFalse(first_hash == new Service("_", "B", "C", "D", "E").hashCode());
      assertFalse(first_hash == new Service("A", "_", "C", "D", "E").hashCode());
      assertFalse(first_hash == new Service("A", "B", "_", "D", "E").hashCode());
      assertFalse(first_hash == new Service("A", "B", "C", "_", "E").hashCode());
      assertFalse(first_hash == new Service("A", "B", "C", "D", "_").hashCode());
   }

   public void testHashCodeChangesWhenSettingStatus() {
      Service service = new Service("A", "B", "C", "D", "E");
      int first_hash = service.hashCode();
      service.setStatus("FOO");
      assertFalse(first_hash == service.hashCode());
   }

   public void testHashCodeChangesWhenSettingLastChecked() {
      Service service = new Service("A", "B", "C", "D", "E");
      int first_hash = service.hashCode();
      service.setLastCheck("FOO");
      assertFalse(first_hash == service.hashCode());
   }

   public void testEquals() {
      Service s = new Service("A", "B", "C", "D", "E");

      assertTrue(s.equals(new Service("A", "B", "C", "D", "E")));

      assertFalse(s.equals(new Service("_", "B", "C", "D", "E")));
      assertFalse(s.equals(new Service("A", "_", "C", "D", "E")));
      assertFalse(s.equals(new Service("A", "B", "_", "D", "E")));
      assertFalse(s.equals(new Service("A", "B", "C", "_", "E")));
      assertFalse(s.equals(new Service("A", "B", "C", "D", "_")));

      assertFalse(s.equals(null));
      assertFalse(s.equals(new Object()));
   }

   public void testEqualsFullfillsEquivalens() {
      Service s1 = new Service("A", "B", "C", "D", "E");
      Service s2 = new Service("A", "B", "C", "D", "E");
      Service s3 = new Service("A", "B", "C", "D", "E");

      // REFLEXIVE (s1 == s1)
      assertTrue(s1.equals(s1));

      // SYMMETRICAL (s1 == s2) -> (s2 == s1)
      assertTrue(s1.equals(s2));
      assertTrue(s2.equals(s1));

      // TRANSITIVE (s1 == s2) && (s2 == s3) -> (s1 == s3)
      assertTrue(s1.equals(s2));
      assertTrue(s2.equals(s3));
      assertTrue(s3.equals(s1));
   }

   public void testEqualsChangesWhenSettingLastChecked() {
      Service s1 = new Service("A", "B", "C", "D", "E");
      Service s2 = new Service("A", "B", "C", "D", "E");
      s2.setLastCheck("FOO");
      assertFalse(s1.equals(s2));
   }
}

