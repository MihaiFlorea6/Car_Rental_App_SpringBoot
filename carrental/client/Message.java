package com.carrental.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Clasa Message - folosită pentru comunicarea prin Socket între Client și Server
 * Implementează Serializable pentru a putea fi transmisă prin ObjectInputStream/ObjectOutputStream
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    // Tipul mesajului (ex: "LOGIN", "GET_CARS", "CREATE_RENTAL", etc.)
    private String type;

    // Date asociate mesajului
    private Map<String, Object> data;

    // Constructori
    public Message() {
        this.data = new HashMap<>();
    }

    public Message(String type) {
        this.type = type;
        this.data = new HashMap<>();
    }

    public Message(String type, Map<String, Object> data) {
        this.type = type;
        this.data = data != null ? data : new HashMap<>();
    }

    // Getters și Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    // Metodă helper pentru adăugare date
    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    // Metodă helper pentru obținere date
    public Object getData(String key) {
        return this.data.get(key);
    }

    @Override
    public String toString() {
        return "Message{type='" + type + "', data=" + data + "}";
    }
}

/**
 * Constante pentru tipurile de mesaje
 */
class MessageType {
    // Autentificare
    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String LOGOUT = "LOGOUT";

    // Operațiuni Autoturisme
    public static final String GET_CARS = "GET_CARS";
    public static final String GET_AVAILABLE_CARS = "GET_AVAILABLE_CARS";
    public static final String ADD_CAR = "ADD_CAR";
    public static final String UPDATE_CAR = "UPDATE_CAR";
    public static final String DELETE_CAR = "DELETE_CAR";

    // Operațiuni Cereri Închiriere
    public static final String CREATE_RENTAL = "CREATE_RENTAL";
    public static final String GET_RENTALS = "GET_RENTALS";
    public static final String UPDATE_RENTAL_STATUS = "UPDATE_RENTAL_STATUS";
    public static final String CANCEL_RENTAL = "CANCEL_RENTAL";

    // Operațiuni Utilizatori
    public static final String GET_USERS = "GET_USERS";
    public static final String CREATE_USER = "CREATE_USER";
    public static final String UPDATE_USER = "UPDATE_USER";
    public static final String DELETE_USER = "DELETE_USER";

    // Răspunsuri
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
}

