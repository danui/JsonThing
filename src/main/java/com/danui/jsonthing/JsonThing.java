package com.danui.jsonthing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JsonThing
 *
 * @author Jin
 */
public class JsonThing {
    public static JsonThing wrap(Object thing) {
        return new JsonThing(thing);
    }

    public static JsonThing newMap() {
        return new JsonThing(new HashMap<String,Object>());
    }

    public static JsonThing newList() {
        return new JsonThing(new ArrayList<Object>());
    }

    private final Object thing;

    private JsonThing(Object thing) {
        this.thing = thing;
    }

    public Object asObject() {
        return thing;
    }

    public Map<String,Object> asMap() {
        return (Map<String,Object>)thing;
    }

    public List<Object> asList() {
        return (List<Object>)thing;
    }

    public String asString() {
        return (String)thing;
    }

    public Long asLong() {
        if (thing instanceof Long) {
            return (Long)thing;
        }
        if (thing instanceof Integer) {
            return Long.valueOf(((Integer)thing).longValue());
        }
        throw new IllegalStateException(
            "thing must be Long or Integer, but it is "+
            thing.getClass().getName());
    }

    public Boolean asBoolean() {
        return (Boolean)thing;
    }

    public long longValue() {
        return asLong();
    }

    public boolean booleanValue() {
        return asBoolean();
    }

    public JsonThing get(String key) {
        return new JsonThing(asMap().get(key));
    }

    public JsonThing get(int idx) {
        return new JsonThing(asList().get(idx));
    }

    public boolean is(String key) {
        Boolean val = (Boolean)(asMap().get(key));
        return (val != null) && val;
    }

    public List<JsonThing> asListOfThings() {
        return asList().stream()
            .map(x -> JsonThing.wrap(x))
            .collect(Collectors.toList());
    }

    public JsonThing put(String key, Object value) {
        if (value instanceof JsonThing) {
            this.asMap().put(key, ((JsonThing)value).asObject());
        } else {
            this.asMap().put(key, value);
        }
        return this;
    }

    public JsonThing add(Object value) {
        if (value instanceof JsonThing) {
            this.asList().add(((JsonThing)value).asObject());
        } else {
            this.asList().add(value);
        }
        return this;
    }

}