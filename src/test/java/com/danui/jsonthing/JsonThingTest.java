package com.danui.jsonthing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * JsonThingTest
 *
 * E10xx Examples
 *
 * - E1001 Example of building an object and reading its values.
 *
 * R10xx Type casting requirements.
 *
 * - R1001 asMap returns a Map
 * - R1002 asList returns a List
 * - R1003 asString returns a String
 * - R1004 asLong returns a Long
 * - R1005 asBoolean returns a Boolean
 * - R1006 longValue returns a long
 * - R1007 booleanValue returns a boolean
 *
 * R20xx Navigation using gets.
 *
 * - R2001 get(key: String) navigates a Map and returns a JsonThing that wraps
 *   the thing at 'key'.
 *
 * - R2002 get(idx: int) navigates a List and returns a JsonThing that wraps the
 *   idx'th element in the list.
 *
 * - R2003 get(key: String) can work with map values of mixed types.
 *
 * - R2004 get(idx: int) can work with list values of mixed types.
 *
 * R30xx Others
 *
 * - R3001 is(key: String) returns true if the thing is a map with a true
 *   boolean at key. is(key: String) should return false otherwise.
 *
 * @author Jin
 */
public class JsonThingTest {

    // - E1001 Example of building an object and reading its values.
    //
    @Test
    public void example_E1001() {
        // For this example we're going to create the following object and then
        // read it.
        //
        // {
        //     "contacts": [
        //         {"name": "Alice", "pin": 9001},
        //         {"name": "Bob", "pin": 9002},
        //         {"name": "Liz", "pin": 5678}
        //     ],
        //     "leader": 5678,
        //     "activated": true
        // }

        // Object Creation
        //
        // A fluent interface is supported allowing for natural object
        // specification. (The put() and get() methods return 'this'.)
        //        
        JsonThing thing = JsonThing.newMap()
            .put("contacts", JsonThing.newList()
                .add(JsonThing.newMap()
                    .put("name", "Alice")
                    .put("pin", 9001))
                .add(JsonThing.newMap()
                    .put("name", "Bob")
                    .put("pin", 9002))
                .add(JsonThing.newMap()
                    .put("name", "Liz")
                    .put("pin", 5678)))
            .put("leader", 5678)
            .put("activated", true);

        assertEquals(
            "The top-level thing should be a Map with "+
            "keys 'contacts', 'leader', and 'activated'",
            new HashSet<>(Arrays.asList("contacts", "leader", "activated")),
            thing.asMap().keySet());

        assertEquals(
            "thing.leader is 5678",
            5678L,
            thing.get("leader").longValue());

        assertEquals(
            "len(thing.contacts) is 3",
            3,
            thing.get("contacts").asList().size());

        assertEquals(
            "thing.contacts[1].name is Bob",
            "Bob",
            thing.get("contacts").get(1).get("name").asString());

        assertEquals(
            "thing.contacts[2].pin is 5678",
            5678L,
            thing.get("contacts").get(2).get("pin").longValue());

        assertTrue(
            "thing.activated is true",
            thing.get("activated").booleanValue());

        assertTrue(
            "thing.activated is true (alternative)",
            thing.is("activated"));

        assertFalse(
            "is() on thing.no_such_property is false "+
            "because there is no such property",
            thing.is("no_such_property"));

        // Iterating through the contacts.
        int idx = 0;
        for (JsonThing contact : thing.get("contacts").asListOfThings()) {
            assertEquals(
                "idx="+idx,
                thing.get("contacts").get(idx).get("name").asString(),
                contact.get("name").asString());
            idx += 1;
        }
    }

    // - R1001 asMap returns a Map
    //
    @Test
    public void test_R1001() throws Exception {
        JsonThing thing = JsonThing.wrap(new HashMap<String,String>());
        assertTrue(thing.asMap() instanceof Map);
    }

    // - R1002 asList returns a List
    //
    @Test
    public void test_R1002() throws Exception {
        JsonThing thing = JsonThing.wrap(new ArrayList<Integer>());
        assertTrue(thing.asList() instanceof List);
    }

    // - R1003 asString returns a String
    //
    @Test
    public void test_R1003() throws Exception {
        JsonThing thing = JsonThing.wrap("some string");
        assertTrue(thing.asString() instanceof String);
    }

    // - R1004 asLong returns a Long
    //
    //   a) when thing is a Long
    //   b) when thing is an Integer
    //
    @Test
    public void test_R1004a() throws Exception {
        JsonThing thing = JsonThing.wrap(Long.valueOf(1));
        assertTrue(thing.asLong() instanceof Long);
    }
    @Test
    public void test_R1004b() throws Exception {
        JsonThing thing = JsonThing.wrap(Integer.valueOf(1));
        assertTrue(thing.asLong() instanceof Long);
    }

    // - R1005 asBoolean returns a Boolean
    //
    @Test
    public void test_R1005() throws Exception {
        JsonThing thing = JsonThing.wrap(Boolean.TRUE);
        assertTrue(thing.asBoolean() instanceof Boolean);
    }

    // - R1006 longValue returns a long
    //
    @Test
    public void test_R1006() throws Exception {
        JsonThing thing = JsonThing.wrap(Long.valueOf(Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, thing.longValue());
    }

    // - R1007 booleanValue returns a boolean
    //
    @Test
    public void test_R1007() throws Exception {
        JsonThing thing = JsonThing.wrap(Boolean.valueOf(Boolean.FALSE));
        assertEquals(Boolean.FALSE, thing.booleanValue());
    }

    // - R2001 get(key: String) navigates a Map and returns a JsonThing that
    //   wraps the thing at 'key'.
    //
    @Test
    public void test_R2001() throws Exception {
        Map<String,String> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", "value2");
        data.put("key3", "value3");
        JsonThing thing = JsonThing.wrap(data);
        assertEquals("value1", thing.get("key1").asString());
        assertEquals("value2", thing.get("key2").asString());
        assertEquals("value3", thing.get("key3").asString());
    }

    // - R2002 get(idx: int) navigates a List and returns a JsonThing that wraps
    //   the idx'th element in the list.
    //
    @Test
    public void test_R2002() throws Exception {
        List<String> data = Arrays.asList("value1", "value2", "value3");
        JsonThing thing = JsonThing.wrap(data);
        assertEquals("value1", thing.get(0).asString());
        assertEquals("value2", thing.get(1).asString());
        assertEquals("value3", thing.get(2).asString());
    }

    // - R2003 get(key: String) can work with map values of mixed types.
    //
    @Test
    public void test_R2003() throws Exception {
        Map<String,Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", 200);
        data.put("key3", false);
        JsonThing thing = JsonThing.wrap(data);
        assertEquals("value1", thing.get("key1").asString());
        assertEquals(200L, thing.get("key2").longValue());
        assertEquals(false, thing.get("key3").booleanValue());
    }

    // - R2004 get(idx: int) can work with list values of mixed types.
    //
    @Test
    public void test_R2004() throws Exception {
        List<Object> data = new ArrayList<>();
        data.add("value1");
        data.add(200);
        data.add(false);
        JsonThing thing = JsonThing.wrap(data);
        assertEquals("value1", thing.get(0).asString());
        assertEquals(200L, thing.get(1).longValue());
        assertEquals(false, thing.get(2).booleanValue());
    }

    // - R3001 is(key: String) returns true if the thing is a map with a true
    //   boolean at key. is(key: String) should return false otherwise.
    //
    //   a) true case.
    //   b) false if the boolean at key is false
    //   c) false if there is no value at key
    //
    @Test
    public void test_R3001() throws Exception {
        Map<String,Boolean> data = new HashMap<>();
        data.put("red", true);
        data.put("blue", false);
        JsonThing thing = JsonThing.wrap(data);
        assertTrue("true if boolean at key is true", thing.is("red"));
        assertFalse("false if boolean at key is false", thing.is("blue"));
        assertFalse("false if no value at key", thing.is("green"));
    }
}