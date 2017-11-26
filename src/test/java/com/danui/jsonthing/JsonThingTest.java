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
 * - R1008 asDouble returns a Double
 * - R1009 doubleValue returns a double
 *
 * R11xx Requirements for ClassCastException.
 *
 * - R1101 asMap throws ClassCastException if the thing is not a Map.
 * - R1102 asList throws ClassCastException if the thing is not a List.
 * - R1103 asString throws ClassCastException if the thing is not a String.
 * - R1104 asLong throws ClassCastException if the thing is not a Long or an
 *   Integer.
 * - R1105 asBoolean throws ClassCastException if the thing is not a Boolean.
 * - R1106 asDouble throws ClassCastException if the thing is not a Double,
 *   Float, Long, or Integer.
 * - R1107 longValue throws ClassCastException if the thing is not a Long or an
 *   Integer.
 * - R1108 doubleValue throws ClassCastException if the thing is not a Double,
 *   Float, Long, or Integer.
 * - R1109 booleanValue throws ClassCastException if the thing is not a Boolean.
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
 * R30xx is()
 *
 * - R3001 is(key: String) returns true if the thing is a map with a true
 *   boolean at key. is(key: String) should return false otherwise.
 *
 * R40xx add() and put()
 *
 * - R4001 add(value: Object) assumes that the wrapped thing is a list and then
 *   appends value to that list. If value is another JsonThing, then the thing
 *   that value wraps would be added instead.
 *
 * - R4002 put(key: String, value: Object) assumes that the wrapped thing is a
 *   map and then puts value into that map at key. If value is another
 *   JsonThing, then the thing that value wraps would be inserted into the map
 *   instead.
 *
 * R50xx JSON parsing and encoding
 *
 * - R5001 JsonThing::parse parses a JSON string into a JsonThing.
 *
 * - R5002 JsonThing::toJson returns the JsonThing as a JSON string.
 *
 * - R5003 JsonThing::toString is not the same as toJson.
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

    // - R1008 asDouble returns a Double
    //
    //   a) when value is a Double
    //   b) when value is a Float
    //   c) when value is a Long
    //   d) when value is an Integer
    //
    @Test
    public void test_R1008a() throws Exception {
        JsonThing thing = JsonThing.wrap(Double.valueOf(11.7));
        assertTrue(thing.asDouble() instanceof Double);
    }
    @Test
    public void test_R1008b() throws Exception {
        JsonThing thing = JsonThing.wrap(Float.valueOf(11.7f));
        assertTrue(thing.asDouble() instanceof Double);
    }
    @Test
    public void test_R1008c() throws Exception {
        JsonThing thing = JsonThing.wrap(Long.valueOf(12L));
        assertTrue(thing.asDouble() instanceof Double);
    }
    @Test
    public void test_R1008d() throws Exception {
        JsonThing thing = JsonThing.wrap(Integer.valueOf(12));
        assertTrue(thing.asDouble() instanceof Double);
    }

    // - R1009 doubleValue returns a double
    //
    @Test
    public void test_R1009() throws Exception {
        JsonThing thing = JsonThing.wrap(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, thing.doubleValue(), 0.0);
    }

    // - R1101 asMap throws ClassCastException if the thing is not a Map.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1101() throws Exception {
        JsonThing.wrap("Map").asMap();
    }

    // - R1102 asList throws ClassCastException if the thing is not a List.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1102() throws Exception {
        JsonThing.wrap("List").asList();
    }

    // - R1103 asString throws ClassCastException if the thing is not a String.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1103() throws Exception {
        JsonThing.wrap(100).asList();
    }

    // - R1104 asLong throws ClassCastException if the thing is not a Long or an
    //   Integer.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1104() throws Exception {
        JsonThing.wrap("Long").asLong();
    }

    // - R1105 asBoolean throws ClassCastException if the thing is not a
    //   Boolean.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1105() throws Exception {
        JsonThing.wrap("Boolean").asBoolean();
    }

    // - R1106 asDouble throws ClassCastException if the thing is not a Double,
    //   Float, Long, or Integer.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1106() throws Exception {
        JsonThing.wrap("Double").asDouble();
    }

    // - R1107 longValue throws ClassCastException if the thing is not a Long or
    //   an Integer.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1107() throws Exception {
        JsonThing.wrap("long").longValue();
    }

    // - R1108 doubleValue throws ClassCastException if the thing is not a
    //   Double, Float, Long, or Integer.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1108() throws Exception {
        JsonThing.wrap("double").doubleValue();
    }

    // - R1109 booleanValue throws ClassCastException if the thing is not a
    //   Boolean.
    //
    @Test(expected=ClassCastException.class)
    public void test_R1109() throws Exception {
        JsonThing.wrap("true").booleanValue();
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
        data.put("key4", 3.14);
        JsonThing thing = JsonThing.wrap(data);
        assertEquals("value1", thing.get("key1").asString());
        assertEquals(200L, thing.get("key2").longValue());
        assertEquals(false, thing.get("key3").booleanValue());
        assertEquals(3.14d, thing.get("key4").doubleValue(), 0.0);
    }

    // - R2004 get(idx: int) can work with list values of mixed types.
    //
    @Test
    public void test_R2004() throws Exception {
        List<Object> data = new ArrayList<>();
        data.add("value1");
        data.add(200);
        data.add(false);
        data.add(3.14);
        JsonThing thing = JsonThing.wrap(data);
        assertEquals("value1", thing.get(0).asString());
        assertEquals(200L, thing.get(1).longValue());
        assertEquals(false, thing.get(2).booleanValue());
        assertEquals(3.14d, thing.get(3).doubleValue(), 0.0);
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

    // - R4001 add(value: Object) assumes that the wrapped thing is a list and
    //   then appends value to that list. If value is another JsonThing, then
    //   the thing that value wraps would be added instead.
    //
    @Test
    public void test_R4001() throws Exception {
        List<Object> list = JsonThing.newList()
            .add("apple")
            .add(JsonThing.wrap("orange"))
            .asList();
        assertEquals("apple", (String)list.get(0));
        assertEquals("orange", (String)list.get(1));
    }

    // - R4002 put(key: String, value: Object) assumes that the wrapped thing is
    //   a map and then puts value into that map at key. If value is another
    //   JsonThing, then the thing that value wraps would be inserted into the
    //   map instead.
    //
    @Test
    public void test_R4002() throws Exception {
        Map<String,Object> map = JsonThing.newMap()
            .put("key1", "apple")
            .put("key2", JsonThing.wrap("orange"))
            .asMap();
        assertEquals("apple", (String)map.get("key1"));
        assertEquals("orange", (String)map.get("key2"));
    }

    // - R5001 JsonThing::parse parses a JSON string into a JsonThing.
    //
    @Test
    public void test_R5001a() throws Exception {
        assertEquals("Empty object", 0, JsonThing.parse("{}").asMap().size());
    }
    @Test
    public void test_R5001b() throws Exception {
        assertEquals("Object with one string property",
            "value1",
            JsonThing.parse("{\"key1\":\"value1\"}").get("key1").asString());
    }
    @Test
    public void test_R5001c() throws Exception {
        assertEquals("Object with one integer property",
            12345L,
            JsonThing.parse("{\"key1\":12345}").get("key1").longValue());
    }
    @Test
    public void test_R5001d() throws Exception {
        assertEquals("Object with one floating point property",
            12.345d,
            JsonThing.parse("{\"key1\":12.345}").get("key1").doubleValue(),
            0.0001);
    }
    @Test
    public void test_R5001e() throws Exception {
        assertEquals("Object with one boolean property",
            false,
            JsonThing.parse("{\"key1\":false}").get("key1").booleanValue());
    }
    @Test
    public void test_R5001f() throws Exception {
        JsonThing obj = JsonThing.parse(
            "{\"values\":[\"a string\",100,12.345,false,{\"x\":1,\"y\":2}]}");
        assertEquals(
            "obj.values[0] is a string",
            "a string",
            obj.get("values").get(0).asString());
        assertEquals(
            "obj.values[1] is 100",
            100L,
            obj.get("values").get(1).longValue());
        assertEquals(
            "obj.values[2] is 12.345",
            12.345d,
            obj.get("values").get(2).doubleValue(),
            0.0001);
        assertEquals(
            "obj.values[3] is false",
            false,
            obj.get("values").get(3).booleanValue());
        assertEquals(
            "obj.values[4].x is 1",
            1L,
            obj.get("values").get(4).get("x").longValue());
        assertEquals(
            "obj.values[4].y is 2",
            2L,
            obj.get("values").get(4).get("y").longValue());
    }

    // - R5002 JsonThing::toJson returns the JsonThing as a JSON string.
    //
    @Test
    public void test_R5002a() throws Exception {
        assertEquals("Empty object", "{}", JsonThing.newMap().toJson());
    }
    @Test
    public void test_R5002b() throws Exception {
        assertEquals("Object with one string property",
            "{\"key1\":\"value1\"}",
            JsonThing.newMap().put("key1", "value1").toJson());
    }
    @Test
    public void test_R5002f() throws Exception {
        JsonThing obj = JsonThing.parse(
            JsonThing.newMap()
                .put("values", JsonThing.newList()
                    .add("a string")
                    .add(100)
                    .add(12.345)
                    .add(false)
                    .add(JsonThing.newMap()
                        .put("x", 1)
                        .put("y", 2)))
                .toJson());
        assertEquals(
            "obj.values[0] is a string",
            "a string",
            obj.get("values").get(0).asString());
        assertEquals(
            "obj.values[1] is 100",
            100L,
            obj.get("values").get(1).longValue());
        assertEquals(
            "obj.values[2] is 12.345",
            12.345d,
            obj.get("values").get(2).doubleValue(),
            0.0001);
        assertEquals(
            "obj.values[3] is false",
            false,
            obj.get("values").get(3).booleanValue());
        assertEquals(
            "obj.values[4].x is 1",
            1L,
            obj.get("values").get(4).get("x").longValue());
        assertEquals(
            "obj.values[4].y is 2",
            2L,
            obj.get("values").get(4).get("y").longValue());
    }

    // - R5003 JsonThing::toString is not the same as toJson.
    //
    @Test
    public void test_R5003() throws Exception {
        JsonThing obj = JsonThing.newMap()
                .put("values", JsonThing.newList()
                    .add("a string")
                    .add(100)
                    .add(12.345)
                    .add(false)
                    .add(JsonThing.newMap()
                        .put("x", 1)
                        .put("y", 2)));
        assertNotEquals(obj.toString(), obj.toJson());
        // System.out.println(obj.toString());
        // System.out.println(obj.toJson());
   }

}