package eni.it.gsrestservice.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVReader {
    private String content;
    private String userID;
    private String role;
    private String group;

    public CSVReader() {

    }

    public int count(byte[] data) throws IOException {
        int count = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
        bufferedReader.readLine();
        while (bufferedReader.readLine() != null) {
            count++;
        }
        return count;
    }

    public CSVReader(CSVReader reader) {
        this.content = reader.content;
        this.userID = reader.userID;
        this.role = reader.role;
        this.group = reader.group;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
