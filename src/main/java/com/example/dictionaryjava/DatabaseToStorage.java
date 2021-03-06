package com.example.dictionaryjava;

import java.sql.*;
import java.util.*;

public class DatabaseToStorage {
    public ArrayList<String> wordStore = new ArrayList<>();
    public HashMap<String,String> meaningStore = new HashMap<>();
    public HashMap<String,String> speechStore = new HashMap<>();
    public HashMap<String,String> typeStore = new HashMap<>();
    /**
     * convert data in database to a storage (hashmap, arraylist) to use later in UX.
     */
    public DatabaseToStorage() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionaryDB", "root", "0912231212Abc");
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("SELECT * FROM dict ORDER BY word");

            while (r.next()) {
                wordStore.add(r.getString("word").toLowerCase(Locale.ROOT));
                speechStore.put(r.getString("word").toLowerCase(Locale.ROOT), r.getString("speech").toLowerCase(Locale.ROOT));
                typeStore.put(r.getString("word").toLowerCase(Locale.ROOT), r.getString("type").toLowerCase(Locale.ROOT));
                meaningStore.put(r.getString("word").toLowerCase(Locale.ROOT), r.getString("meaning").toLowerCase(Locale.ROOT));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkDuplicate(String word) {
        return wordStore.contains(word);
    }

    /**
     * print out the database storage.
     * @return the storage (word, speech, type, meaning)
     */
    public ArrayList<String> getWordStore() {
        return wordStore;
    }

    public HashMap<String, String> getMeaning() {
        return meaningStore;
    }

    public HashMap<String, String> getSpeech() {
        return speechStore;
    }

    public HashMap<String, String> getType() {
        return typeStore;
    }
}
