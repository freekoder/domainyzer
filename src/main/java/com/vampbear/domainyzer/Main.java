package com.vampbear.domainyzer;


public class Main {
    public static void main(String[] args) {
        String domain = "gazprom.ru";
        System.out.println("Domainyzer");
        System.out.println("Resolving domain: " + domain);
        Domainyzer domainyzer = new Domainyzer();
        ReconResult result = domainyzer.fromDomain(domain);
        for (Subdomain subdomain : result.getSubdomains()) {
            System.out.println(subdomain.getSubdomain() + " " + subdomain.getSources());
        }
        System.out.println("Found " + result.getSubdomains().size() + " subdomains");
        if (result.getErrors().size() > 0) {
            System.out.println("Has errors:");
            for (SourceError error : result.getErrors()) {
                System.out.println("\tERROR: [" + error.getSourceName() + "] " + error.getErrorMessage());
            }
        }
    }
}
