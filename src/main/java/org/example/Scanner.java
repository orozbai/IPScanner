package org.example;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scanner {
    private final String ipAddress;
    private final int numThreads;

    public Scanner(String ipAddress, int numThreads) {
        this.ipAddress = ipAddress;
        this.numThreads = numThreads;
    }

    public void startScanning() {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        String[] ipParts = ipAddress.split("\\.");
        int startIp = Integer.parseInt(ipParts[3].split("/")[0]);
        int endIp = startIp + (int) Math.pow(2, (32 - Integer.parseInt(ipParts[3].split("/")[1])));

        for (int i = startIp; i <= endIp; i++) {
            String currentIp = ipParts[0] + "." + ipParts[1] + "." + ipParts[2] + "." + i;

            executorService.execute(() -> {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("found_domains.txt", true));

                    URL url = new URL("https://" + currentIp);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.connect();

                    Certificate[] certs = connection.getServerCertificates();
                    System.out.println("Успешно подключено к: " + currentIp);
                    for (Certificate cert : certs) {
                        String name = "ip: " + currentIp + " Имя домена: " + ((X509Certificate) cert).getSubjectAlternativeNames();
                        writer.write(name);
                        writer.newLine();
                    }
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Не удалось подключиться к: " + currentIp);
                }
            });
        }
    }
}
