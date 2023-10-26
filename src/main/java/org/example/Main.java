package org.example;

import io.javalin.Javalin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/", context -> {
            String htmlContent = new String(Files.readAllBytes(Paths.get("src/web/index.html")));
            context.contentType("text/html").result(htmlContent);
        });

        app.post("/scan", context -> {
            String ipAddressStr = context.formParam("ipAddress");
            int num = Integer.parseInt(Objects.requireNonNull(context.formParam("numThreads")));

            Scanner scanner = new Scanner(ipAddressStr, num);
            scanner.startScanning();
            context.result("Scanning in progress, wait please");
        });
    }
}