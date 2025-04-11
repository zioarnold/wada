package eni.it.gsrestservice.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.SecureRandom;

@Component
public class QlikSenseConnector {
    private static String xrfKey, host, proxyCert, rootCert, rootCertPwd, userHeader, qsReloadTaskName;
    private static SSLSocketFactory sslSocketFactory;

    public static void initConnector(final String key, final String host, final String proxyCert,
                                     final String rootCert, final String rootCertPwd, final String userHeader,
                                     final String qsReloadTaskName) {
        QlikSenseConnector.xrfKey = key;
        QlikSenseConnector.host = host;
        QlikSenseConnector.proxyCert = proxyCert;
        QlikSenseConnector.rootCert = rootCert;
        QlikSenseConnector.rootCertPwd = rootCertPwd;
        QlikSenseConnector.userHeader = userHeader;
        QlikSenseConnector.qsReloadTaskName = qsReloadTaskName;
    }

    public static void configureCertificate() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(Files.newInputStream(new File(proxyCert).toPath()), rootCertPwd.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, rootCertPwd.toCharArray());
            SSLContext sslContext = SSLContext.getInstance("SSL");
            KeyStore keyStoreTrust = KeyStore.getInstance("JKS");
            keyStoreTrust.load(Files.newInputStream(new File(rootCert).toPath()), rootCertPwd.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStoreTrust);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
            System.setProperty("javax.net.ssl.keyStore", proxyCert);
            System.setProperty("javax.net.ssl.keyStorePassword", rootCertPwd);
            System.setProperty("javax.net.ssl.trustStore", rootCert);
            System.setProperty("javax.net.ssl.trustStorePassword", rootCertPwd);
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUserRoleByUserId(String userId, String role) throws Exception {
        String spec = "https://" + host + ":4242/qrs/user/full?filter=userId eq '" + userId + "'&xrfkey=" + xrfKey;
        String replace = spec.replace(" ", "%20").replace("'", "%27");
        HttpsURLConnection connection = (HttpsURLConnection) new URL(replace).openConnection();
        connection.setSSLSocketFactory(sslSocketFactory);
        connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
        connection.setRequestProperty("X-Qlik-User", userHeader);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("PUT");
        String data = obtainHttpConnection(connection);
        JSONObject jsonObject = new JSONObject(data);
        JSONArray attributes = jsonObject.getJSONArray("attributes");
        for (int i = 0; i < attributes.length(); i++) {
            attributes.getJSONObject(i).remove("modifiedDate");
        }
        jsonObject.put("attributes", attributes);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(role);
        jsonObject.put("roles", jsonArray);
    }

    public String getUserRoleByUserId(String userId) throws Exception {
        String spec = "https://" + host + ":4242/qrs/user/full?filter=userId eq '" + userId + "'&xrfkey=" + xrfKey;
        String replace = spec.replace(" ", "%20").replace("'", "%27");
        URL url = new URL(replace);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sslSocketFactory);
        connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
        connection.setRequestProperty("X-Qlik-User", userHeader);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");
        String data = obtainHttpConnection(connection);
        JSONArray jsonArray = new JSONArray(data);
        if (jsonArray.isEmpty()) {
            return "N/A";
        } else {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.isEmpty()) {
                return "N/A";
            } else {
                JSONArray roles = jsonObject.getJSONArray("roles");
                return roles.getString(0);
            }
        }
    }

    private String obtainHttpConnection(HttpsURLConnection connection) throws IOException {
        if (connection != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            in.close();
            return builder.toString();
        } else return "";
    }

    public int ping() {
        String pingURL = "https://" + host + ":4242/ssl/ping?xrfkey=" + xrfKey;
        HttpsURLConnection connection;
        try {
            connection = (HttpsURLConnection) new URL(pingURL).openConnection();
            connection.setSSLSocketFactory(sslSocketFactory);
            connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
            connection.setRequestProperty("X-Qlik-User", userHeader);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");
            return connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            return 200;
        }
    }

    public int startReloadTask() throws IOException {
        String reloadTaskURL = "https://" + host + ":4242/qrs/task/start/synchronous/?name=UDC Sync_usersynctask&xrfkey=" + xrfKey;
        HttpsURLConnection connection = (HttpsURLConnection) new URL(reloadTaskURL.replace(" ", "%20")).openConnection();
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
        connection.setRequestProperty("X-Qlik-User", userHeader);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        sendData(connection, "");
        return connection.getResponseCode();
    }

    private static void sendData(HttpsURLConnection connection, String data) {
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(data);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(dataOutputStream);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {

        }
    }
}
