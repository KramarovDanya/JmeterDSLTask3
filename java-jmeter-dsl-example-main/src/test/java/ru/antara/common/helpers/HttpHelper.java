package ru.antara.common.helpers;

//import jetbrains.datalore.base.observable.property.Properties;
import net.datafaker.providers.base.File;
import us.abstracta.jmeter.javadsl.http.DslHttpDefaults;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;


import static us.abstracta.jmeter.javadsl.JmeterDsl.httpDefaults;

public class HttpHelper {

//    private static int port;
//
//   public static int initializePort(){
//        Properties properties = new Properties();
//        try (InputStream input = HttpHelper.class.getClassLoader().getResourceAsStream("common_test.properties")){
//            if (input != null){
//                properties.load(input);
//                String portValue = properties.getProperty("PORT");
//                if (portValue != null){
//                    port = Integer.parseInt(portValue);
//                    return port;
//                }
//            }else {
//                throw  new IOException("Unable to find file");
//            }
//        }catch (IOException | NumberFormatException e){
//            System.out.println("Error loading or parsing properties file" + e.getMessage());
//        }
//        return port;
//    }

    private static int port;

    public static void initializePort(int portValue) {
        port = portValue;
    }

    public static DslHttpDefaults getHttpDefaults() {

        return httpDefaults()
                .protocol("${__P(PROTOCOL)}")
                .host("${__P(HELPDESK_HOST)}")
                .port(port)
                .encoding(StandardCharsets.UTF_8)
                .connectionTimeout(Duration.ofSeconds(3))
                .responseTimeout(Duration.ofSeconds(10));
    }

}
