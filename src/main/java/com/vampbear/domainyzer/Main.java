package com.vampbear.domainyzer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Domainyzer");
        Domainyzer domainyzer = new Domainyzer();
        List<String> subdomains = domainyzer.fromDomain("hackerone.com");
        for (String subdomain : subdomains) {
            System.out.println(subdomain);
        }
    }
}
