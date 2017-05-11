package com.treelogic.proteus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by ignacio.g.fernandez on 3/05/17.
 */
public class ProteusData {

    public static  int TIME_BETWEEN_COILS;

    public static  int COIL_TIME;

    private static Map<String, String> coil_xMax = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ProteusData.class);

    private static Properties properties;

    public static List<String> FLATNESS_VARNAMES = Arrays.asList("C0042", "C0028", "C0011");


    static {
        properties = new Properties();
        try {
            properties.load(ProteusData.class.getClassLoader().getResource("config.properties").openStream());
            TIME_BETWEEN_COILS = Integer.parseInt((String)ProteusData.get("model.timeBetweenCoils"));
            COIL_TIME = Integer.parseInt((String)ProteusData.get("model.coilTime"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object get(String property) {
        return properties.get("com.treelogic.proteus." + property);
    }

    public static Double getXmax(int coil) {
        String coilString = String.valueOf(coil);
        Double maxX = Double.parseDouble(coil_xMax.get(coilString));
        return maxX;
    }

    public static void loadData() {
        ClassLoader classLoader = ProteusData.class.getClassLoader();
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(classLoader.getResource("PROTEUS-maxX.json").toURI())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            coil_xMax = new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}