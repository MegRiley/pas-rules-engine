package org.hl7.davinci.rules;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

class PropertyProvider {

   // private static final Logger logger = PALogger.getLogger();

    private static final String PROPERTY_FILE = "config.properties";

    public static String getProperty(String property) {
        String result = null;
        try {
            InputStream inputStream = new FileInputStream(PROPERTY_FILE);
            Properties properties = new Properties();
            properties.load(inputStream);

            result = properties.getProperty(property);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
     //   logger.info("RulesProperyProvider::getProperty(" + property + "):" + result);
        return result;
    }

}