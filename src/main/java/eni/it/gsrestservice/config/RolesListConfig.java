package eni.it.gsrestservice.config;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class RolesListConfig {

    @Value("${log.role.exist.for.user}")
    public static String roleExist;

    @Value("${roles.config.json.path}")
    private String rolesFilePath;

    public List<String> initRolesList() throws IOException {
        List<String> list = new ArrayList<>();
        File rolesFile = new File(rolesFilePath);
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
        return list;
    }
}
