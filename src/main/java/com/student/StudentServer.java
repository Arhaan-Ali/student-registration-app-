package com.student;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StudentServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
        server.createContext("/", ex -> {
            if ("/health".equals(ex.getRequestURI().getPath())) { send(ex, 200, "text/plain", "OK"); return; }
            if ("/register".equals(ex.getRequestURI().getPath()) && "POST".equals(ex.getRequestMethod())) {
                Map<String,String> f = parse(new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String html = "<!DOCTYPE html><html><head><title>Success</title></head><body>"
                    + "<h2>Registration Successful!</h2>"
                    + "<p><b>Student Name:</b> " + esc(f.getOrDefault("studentName","")) + "</p>"
                    + "<p><b>Roll Number:</b> " + esc(f.getOrDefault("rollNumber","")) + "</p>"
                    + "<p><b>Email:</b> " + esc(f.getOrDefault("email","")) + "</p>"
                    + "<p><b>Course:</b> " + esc(f.getOrDefault("course","")) + "</p>"
                    + "<a href='/'>Register another student</a></body></html>";
                send(ex, 200, "text/html", html); return;
            }
            try (InputStream in = StudentServer.class.getResourceAsStream("/index.html")) {
                send(ex, 200, "text/html", new String(in.readAllBytes(), StandardCharsets.UTF_8));
            }
        });
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
        System.out.println("Student Registration App running at http://localhost:" + port);
    }
    static Map<String,String> parse(String body) throws java.io.IOException {
        Map<String,String> m = new HashMap<>();
        for (String p : body.split("&")) { String[] kv = p.split("=",2);
            if (kv.length==2) m.put(URLDecoder.decode(kv[0],"UTF-8"), URLDecoder.decode(kv[1],"UTF-8")); }
        return m;
    }
    static String esc(String s){ return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); }
    static void send(HttpExchange ex,int code,String type,String body) throws java.io.IOException {
        byte[] b = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", type+"; charset=UTF-8");
        ex.sendResponseHeaders(code, b.length); ex.getResponseBody().write(b); ex.close();
    }
}
