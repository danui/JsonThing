# JsonThing

A utility for working with JSON strings.

## Building JSON objects using JsonThing

In this example the objective is to create a JSON object with the
following structure.

    {
        "name": "Alice",
        "badge_number": 107,
        "active": false,
        "district_ids": [310, 309, 308],
        "accuracy": 83.128
    }

JsonThing provides a fluent interface that enables a natural approach
for constructing arbitrary JSON object structures. The above structure
would be created as follows.

    JsonThing obj = JsonThing.newMap()
        .put("name", "Alice")
        .put("badge_number", 107)
        .put("active", false)
        .put("district_ids", JsonThing.newList()
            .add(310)
            .add(309)
            .add(308))
        .put("accuracy", 83.128);

## Accessing values of a JsonThing

Assuming that a JsonThing was constructed with the following structure.

    {
        "name": "Alice",
        "badge_number": 107,
        "active": false,
        "district_ids": [310, 309, 308],
        "accuracy": 83.128
    }

The following assertions show how individual property values can be
read.

    assertEquals("Alice", obj.get("name").asString());
    assertEquals(107L, obj.get("badge_number").longValue());
    assertFalse(obj.get("active").booleanValue());
    assertFalse(obj.is("active"));
    assertEquals(310L, obj.get("district_ids").get(0).longValue());
    assertEquals(309L, obj.get("district_ids").get(1).longValue());
    assertEquals(308L, obj.get("district_ids").get(2).longValue());
    assertEquals(83.128d, obj.get("accuracy").doubleValue(), 0.00001);

## Encoding JsonThing to JSON string

Given a JsonThing object `obj`, the following encodes `obj` into a JSON
string `s`.

    try {
        s = obj.toJson();
    } catch (IOException e) {
        // Failed to encode.
    }

## Parsing JSON string to JsonThing

Given a JSON string `s`, the following parses `s` into a JsonThing
`obj`.

    try {
        JsonThing obj = JsonThing.parse(s);
    } catch (IOException e) {
        // Failed to parse.
    }

## Specification

The definitive specification is provided by the unit test
[`JsonThingTest.java`](src/test/java/com/danui/jsonthing/JsonThingTest.java)
