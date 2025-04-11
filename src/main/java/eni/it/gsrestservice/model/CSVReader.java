package eni.it.gsrestservice.model;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Setter
@Getter
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

}
