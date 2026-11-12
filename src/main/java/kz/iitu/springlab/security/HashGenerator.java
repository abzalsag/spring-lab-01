package kz.iitu.springlab.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("user123"));
        System.out.println(encoder.encode("user123"));   // bir parol, basqa hash
        System.out.println(encoder.encode("admin123"));
    }
}