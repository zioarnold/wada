package eni.it.gsrestservice.model;

import eni.it.gsrestservice.config.LoggingMisc;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

public class QlikSenseConnector {
    private static String xrfKey, host, proxyCert, rootCert, rootCertPwd, userHeader, qsReloadTaskName;
    private static SSLSocketFactory sslSocketFactory;
    private final LoggingMisc loggingMisc;

    public QlikSenseConnector() {
        loggingMisc = new LoggingMisc();
    }

    public void initConnector(final String key, final String host, final String proxyCert,
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

    public void configureCertificate() throws Exception {
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configuring keystore instance for " + proxyCert + ": JKS");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configuring keystore instance: JKS - successful");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - loading keystore: " + proxyCert);
        keyStore.load(new FileInputStream(new File(proxyCert)), rootCertPwd.toCharArray());
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - loading keystore: " + proxyCert + " successful");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, rootCertPwd.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("SSL");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configuring keystore instance for " + rootCert + ": JKS");
        KeyStore keyStoreTrust = KeyStore.getInstance("JKS");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configuring keystore instance for " + rootCert + ": JKS - successful");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - loading keystore: " + rootCert);
        keyStoreTrust.load(new FileInputStream(new File(rootCert)), rootCertPwd.toCharArray());
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - loading keystore: " + rootCert + " successful");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStoreTrust);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configuring certificates: " + proxyCert + ";" + rootCert);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configuring certificates: " + proxyCert + ";" + rootCert + " successful");
        sslSocketFactory = sslContext.getSocketFactory();
        System.setProperty("javax.net.ssl.keyStore", proxyCert);
        System.setProperty("javax.net.ssl.keyStorePassword", rootCertPwd);
        System.setProperty("javax.net.ssl.trustStore", rootCert);
        System.setProperty("javax.net.ssl.trustStorePassword", rootCertPwd);
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
    }

    public void setUserRoleByUserId(String userId, String role) throws Exception {
        String spec = "https://" + host + ":4242/qrs/user/full?filter=userId eq '" + userId + "'&xrfkey=" + xrfKey;
        String replace = spec.replace(" ", "%20").replace("'", "%27");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configuring url: " + replace);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Opening connection");
        HttpsURLConnection connection = (HttpsURLConnection) new URL(replace).openConnection();
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Opening connection successful");
        connection.setSSLSocketFactory(sslSocketFactory);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Configure headers: " + xrfKey + " " + userHeader);
        connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
        connection.setRequestProperty("X-Qlik-User", userHeader);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("PUT");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        in.close();
        String data = builder.toString();
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " Getting data: " + data);
        JSONObject jsonObject = new JSONObject(data);
        JSONArray attributes = jsonObject.getJSONArray("attributes");
        for (int i = 0; i < attributes.length(); i++) {
            attributes.getJSONObject(i).remove("modifiedDate");
        }
        jsonObject.put("attributes", attributes);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(role);
        jsonObject.put("roles", jsonArray);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " Sending data: " + jsonObject);
    }

    public String getUserRoleByUserId(String userId) throws Exception {
        String spec = "https://" + host + ":4242/qrs/user/full?filter=userId eq '" + userId + "'&xrfkey=" + xrfKey;
        String replace = spec.replace(" ", "%20").replace("'", "%27");
        URL url = new URL(replace);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Executing URL: " + replace);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sslSocketFactory);
        connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
        connection.setRequestProperty("X-Qlik-User", userHeader);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        in.close();
        String data = builder.toString();
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

    public int ping() throws IOException {
        String pingURL = "https://" + host + ":4242/ssl/ping?xrfkey=" + xrfKey;
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Executing URL: " + pingURL);
        HttpsURLConnection connection = (HttpsURLConnection) new URL(pingURL).openConnection();
        connection.setSSLSocketFactory(sslSocketFactory);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up header X-Qlik-Xrfkey: " + xrfKey);
        connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up header X-Qlik-User: " + userHeader);
        connection.setRequestProperty("X-Qlik-User", userHeader);
        connection.setDoInput(true);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up request property: Content-Type application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up request method: GET");
        connection.setRequestMethod("GET");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Connection response code: " + connection.getResponseCode());
        return connection.getResponseCode();
    }

    public int startReloadTask() throws IOException {
        String reloadTaskURL = "https://" + host + ":4242/qrs/task/start/synchronous/?name=UDC Sync_usersynctask&xrfkey=" + xrfKey;
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Executing URL: " + reloadTaskURL.replace(" ", "%20"));
        HttpsURLConnection connection = (HttpsURLConnection) new URL(reloadTaskURL.replace(" ", "%20")).openConnection();
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up SSLFactory: " + sslSocketFactory.toString());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up header X-Qlik-Xrfkey: " + xrfKey);
        connection.setRequestProperty("X-Qlik-Xrfkey", xrfKey);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up header X-Qlik-User: " + userHeader);
        connection.setRequestProperty("X-Qlik-User", userHeader);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up request property: Content-Type application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Setting up request method: POST");
        connection.setRequestMethod("POST");
        this.sendData(connection, "");
        loggingMisc.printConsole(1, QlikSenseConnector.class.getSimpleName() + " - Connection response code: " + connection.getResponseCode());
        return connection.getResponseCode();
    }

    private void sendData(HttpsURLConnection connection, String data) {
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(data);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.closeQuietly(dataOutputStream);
        }
    }

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ex) {

        }
    }
}