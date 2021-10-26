package com.example.dictionaryjava;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {
    private static Pattern pattern = Pattern.compile("/.*/");
    private static String wordToInsert = "";
    private static StringBuilder speechToInsert = new StringBuilder();
    private static StringBuilder meaningToInsert = new StringBuilder();
    private static StringBuilder typeToInsert = new StringBuilder();


    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionaryDB", "root", "0912231212Abc");
                Statement stat = con.createStatement();
                inputStream = new FileInputStream("src/main/resources/db-in-text/en_vi.txt");
                sc = new Scanner(inputStream, StandardCharsets.UTF_8);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();

                    if (Objects.equals(line, "")) {
                        line = sc.nextLine();
                        String command = "INSERT INTO dict (word, speech, type, meaning) VALUES (?, ?, ?, ?)";
                        PreparedStatement statement = con.prepareStatement(command);
                        statement.setString(1, wordToInsert);
                        statement.setString(2, speechToInsert.toString());
                        statement.setString(3, typeToInsert.toString());
                        statement.setString(4, meaningToInsert.toString());
                        statement.execute();
                        System.out.println(wordToInsert);
                        System.out.println(speechToInsert);
                        System.out.println(typeToInsert);
                        System.out.println(meaningToInsert);
                    }
                    if (!sc.hasNextLine()) {
                        System.out.println(wordToInsert);
                        System.out.println(speechToInsert);
                        System.out.println(typeToInsert);
                        System.out.println(meaningToInsert);
                        break;
                    }
                    if (line.startsWith("@")) {
                        wordToInsert = "";
                        speechToInsert = new StringBuilder();
                        meaningToInsert = new StringBuilder();
                        typeToInsert = new StringBuilder();
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String word = line.replace(line.substring(matcher.start(), matcher.end()), "");
                            word = word.substring(1, word.length() - 1);
                            wordToInsert = word;
                            System.out.println(wordToInsert);
                            speechToInsert.append(line.substring(matcher.start(), matcher.end())).append("\n");
                            line = sc.nextLine();
                        }
                    }
                    if (line.startsWith("*")) {
                        if(line.contains("&")) {
                            line = line.replace(" &", ",");
                        }
                        line = line.substring(3);
                        typeToInsert.append(line).append("\n");
                        line = sc.nextLine();
                    }

                    if (line.startsWith("=")) {
                        line = line.replace("=", " → ");
                        line = line.replace("+", ":");
                    }
                    if (line.startsWith("!")) {
                        line = line.replace("!", "  > Idiom: ");
                    }
                    meaningToInsert.append(line).append("\n");

                    if (sc.ioException() != null) {
                        throw sc.ioException();
                    }
                }
            } catch (
                    SQLException e) {
                System.out.println(e.getMessage());
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }
}


