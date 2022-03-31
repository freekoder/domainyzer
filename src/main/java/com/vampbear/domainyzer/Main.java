package com.vampbear.domainyzer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String domain = "hackerone.com";
        System.out.println("Domainyzer");
        Domainyzer domainyzer = new Domainyzer();
        List<String> subdomains = domainyzer.fromDomain(domain);
        for (String subdomain : subdomains) {
            System.out.println(subdomain);
        }
        System.out.printf("Found %d subdomains for %s%n", subdomains.size(), domain);
    }
}
