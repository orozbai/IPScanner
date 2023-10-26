package org.example;

import io.javalin.Javalin;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/", context -> {
            String htmlContent = new String(Files.readAllBytes(Paths.get("src/web/index.html")));
            context.contentType("text/html").result(htmlContent);
        });

        app.post("/scan", context -> {
            String ipAddressStr = context.formParam("ipAddress");
            InetAddress ipAddress = InetAddress.getByName(ipAddressStr);
            int num = Integer.parseInt(context.formParam("numThreads"));


        });
    }
}