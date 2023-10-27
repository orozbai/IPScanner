package org.example;

import io.javalin.Javalin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        TrustAllCerts.install();
        Javalin app = Javalin.create().start(7000);
        app.get("/", context -> {
            String htmlContent = new String(Files.readAllBytes(Paths.get("src/web/index.html")));
            context.contentType("text/html").result(htmlContent);
        });

        app.get("/result", context -> {
            String htmlContent = new String(Files.readAllBytes(Paths.get("src/web/result.html")));
            context.contentType("text/html").result(htmlContent);
        });

        app.get("/getResult", ctx -> {
            ctx.async(() -> {
                try {
                    String content = new String(Files.readAllBytes(Paths.get("found_domains.txt")));
                    ctx.contentType("text/plain");
                    ctx.result(content);
                } catch (Exception e) {
                    e.printStackTrace();
                    ctx.status(500).result("Ошибка при чтении файла");
                }
            });
        });

        app.post("/scan", context -> {
            String ipAddressStr = context.formParam("ipAddress");
            int num = Integer.parseInt(Objects.requireNonNull(context.formParam("numThreads")));

            Scanner scanner = new Scanner(ipAddressStr, num);
            if (scanner.startScanning()) {
                context.redirect("/result");
            } else {
                context.redirect("/");
            }
        });
    }
}