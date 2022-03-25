package eni.it.gsrestservice.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RolesListConfig {
    private List<String> list;
    private File rolesFile;
    private BufferedReader reader;
    private StringBuilder stringBuilder;
    private JSONObject jsonObject;
    private JSONArray roles;

    public RolesListConfig() {
        list = new ArrayList<>();
    }

    public List<String> initRolesList(String path) throws IOException {
        list.clear();
        rolesFile = new File(path);
        reader = new BufferedReader(new FileReader(rolesFile));
        stringBuilder = new StringBuilder();
        String t;
        while ((t = reader.readLine()) != null) {
            stringBuilder.append(t);
        }
        reader.close();
        jsonObject = new JSONObject(stringBuilder.toString());
        roles = jsonObject.getJSONArray("roles");
        for (int i = 0; i < roles.length(); i++) {
            list.add(roles.getString(i));
        }
        return list;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
