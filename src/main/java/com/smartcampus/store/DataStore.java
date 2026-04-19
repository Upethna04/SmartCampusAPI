/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();
    private final ConcurrentHashMap<String, Sensor> sensors = new ConcurrentHashMap<String, Sensor>();
    private final ConcurrentHashMap<String, List<SensorReading>> readings = new ConcurrentHashMap<String, List<SensorReading>>();

    private DataStore() {
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Lab", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "ACTIVE", 400.0, "LAB-101");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);

        r1.getSensorIds().add(s1.getId());
        r2.getSensorIds().add(s2.getId());

        List<SensorReading> r1readings = new ArrayList<SensorReading>();
        List<SensorReading> r2readings = new ArrayList<SensorReading>();
        readings.put(s1.getId(), r1readings);
        readings.put(s2.getId(), r2readings);
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public ConcurrentHashMap<String, Room> getRooms() {
        return rooms;
    }

    public ConcurrentHashMap<String, Sensor> getSensors() {
        return sensors;
    }

    public ConcurrentHashMap<String, List<SensorReading>> getReadings() {
        return readings;
    }
}