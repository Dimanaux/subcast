package com.example.subcast.controllers;

import java.util.Map;
import java.util.TreeMap;

public interface CommonResponses {
    Map<String, Object> INVALID_TOKEN = new TreeMap<String, Object>() {{
        put("status", "ERROR");
        put("errorMessage", "Invalid token.");
    }};

    Map<String, Object> STATUS_OK = new TreeMap<String, Object>() {{
        put("status", "OK");
    }};

    Map<String, Object> STATUS_ERROR = new TreeMap<String, Object>() {{
        put("status", "ERROR");
    }};

}
