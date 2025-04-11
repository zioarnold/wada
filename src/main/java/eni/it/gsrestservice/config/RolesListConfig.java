package eni.it.gsrestservice.config;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "roles")
@Getter
public class RolesListConfig {
    private final List<String> list;

    @Value("${roles.config.json.path}")
    private String rolesFilePath;

    public RolesListConfig() {
        this.list = new ArrayList<>();
        try {
            initRolesList(rolesFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize roles list", e);
        }
    }

    private void initRolesList(String path) throws IOException {
        list.clear();
        File rolesFile = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(rolesFile))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray roles = jsonObject.getJSONArray("roles");
            for (int i = 0; i < roles.length(); i++) {
                list.add(roles.getString(i));
            }
        }
    }
}
