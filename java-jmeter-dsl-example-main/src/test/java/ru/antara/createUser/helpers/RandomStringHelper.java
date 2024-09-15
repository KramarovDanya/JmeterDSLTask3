package ru.antara.createUser.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class RandomStringHelper {
    public static void writeFile(String login, String password, String filename) {
        boolean fileExists = new java.io.File(filename).exists();
        try (FileWriter writer = new FileWriter(filename, true)) {
            if (!fileExists) {
                writer.append("Login,Password\n");
            }
            writer.append(login).append(",").append(password).append("\n");
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Error write from file" + filename);
        }
    }




    public static String generateRandomString(int lenth){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(lenth);

        for (int i = 0; i < lenth; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(chars.length());
            randomString.append(chars.charAt(randomIndex));
        }

        return randomString.toString();
    }

    public static String getLogin(String[] credentials) {
        String login;
        if (credentials.length == 2) {
            login = credentials[0];
        } else login = "ErrorLogin";
        return login;
    }

    public static String getPassword(String[] credentials) {
        String password;
        if (credentials.length == 2) {
            password = credentials[1];
        } else password = "ErrorPassword";
        return password;
    }
    public static String[] readFile(String filename) {
        String[] logAndPassMassive = new String[2];

        try (BufferedReader breader = new BufferedReader(new FileReader(filename))) {
            String line;
            breader.readLine();
            if ((line = breader.readLine()) != null) {
                logAndPassMassive = line.split(",");
            }
        } catch (IOException e) {
            System.out.println("Error read from file"+ e.getMessage());
        }
        return logAndPassMassive;
    }

}
