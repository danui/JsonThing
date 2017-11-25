# JsonThing

Java utility for reading and writing "JSON things" such as Map and List.

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

Wrapping...

    Map<String,Object> employeeData = ...; // from Jackson
    JsonThing employee = JsonThing.wrap(employeeData);

Reading...

    assertEquals("Alice", employee.get("name").asString());
    assertEquals(107L, employee.get("badge_number").longValue());
    assertEquals(310L, employee.get("district_ids").get(0).longValue());
    assertEquals(309L, employee.get("district_ids").get(1).longValue());
    assertEquals(308L, employee.get("district_ids").get(2).longValue());
    assertFalse(employee.is("active"));
    assertFalse(employee.get("active").booleanValue());

We can also use JsonThing to create such maps in a very fluent manner.

    JsonThing employee = JsonThing.newMap()
        .put("name", "Alice")
        .put("badge_number", 107)
        .put("district_ids", JsonThing.newList()
            .add(310)
            .add(309)
            .add(308))
        .put("active", false);

The above leaves us with a JsonThing. To get a Map map, just call asMap.

    Map<String,Object> employeeData = employee.asMap();
