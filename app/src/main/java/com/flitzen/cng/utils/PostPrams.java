package com.flitzen.cng.utils;

public class PostPrams {
    String key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String value;

    public static PostPrams NewPair(String Key, String Value) {
        PostPrams postPrams = new PostPrams();
        postPrams.setKey(Key);
        postPrams.setValue(Value);
        return postPrams;
    }
}
