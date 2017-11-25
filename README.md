# JsonThing

Most libraries that work with JSON define polymorphic data types such as
JsonObject, JsonArray, JsonNumber, etc.  I think we can simplify that by
having just a single JsonThing.  Whatever type a thing may be.  Okay
maybe it is not that flexible.  JsonThing is a reader for JSON strings
that have been expressed into Map and List form where...

- JSON Object is a `Map<String,Object>`
- JSON Array is a `List<Object>`
- JSON String is a `String`
- JSON Number is a `Long` or `Double`
- JSON Boolean is a `Boolean`

Suppose that we have used Jackson to transform the following JSON string
into a Map.

    {
        "name": "Alice",
        "badge_number": 107,
        "district_ids": [310, 309, 308],
        "active": false
    }

Then to read it we can just wrap the map using `JsonThing::wrap`.

    Map<String,Object> employeeData = ...; // from Jackson
    JsonThing employee = JsonThing.wrap(employeeData);

Values are then read using `get(String)` or `get(int)` to navigate and
various type casting methods (e.g. `asString`, `longValue` etc.) to read
the value.

Reading name and badge number...

    assertEquals("Alice", employee.get("name").asString());
    assertEquals(107L, employee.get("badge_number").longValue());

Reading district IDs from an array...

    assertEquals(310L, employee.get("district_ids").get(0).longValue());
    assertEquals(309L, employee.get("district_ids").get(1).longValue());
    assertEquals(308L, employee.get("district_ids").get(2).longValue());

Reading a boolean...

    assertFalse(employee.get("active").booleanValue());

A shorthand for reading booleans...

    assertFalse(employee.is("active"));

We can also use JsonThing to create such maps in a very fluent manner.
First we make a JsonThing.

    JsonThing employee = JsonThing.newMap()
        .put("name", "Alice")
        .put("badge_number", 107)
        .put("district_ids", JsonThing.newList()
            .add(310)
            .add(309)
            .add(308))
        .put("active", false);

Then we call asMap to cast its underlying object to a Map.

    Map<String,Object> employeeData = employee.asMap();

Or all at once...

    Map<String,Object> employeeData = JsonThing.newMap()
        .put("name", "Alice")
        .put("badge_number", 107)
        .put("district_ids", JsonThing.newList()
            .add(310)
            .add(309)
            .add(308))
        .put("active", false)
        .asMap();
