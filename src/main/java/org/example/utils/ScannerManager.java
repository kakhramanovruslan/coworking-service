package org.example.utils;

import java.util.Scanner;

public class ScannerManager {
    private final static ScannerManager scannerManager = new ScannerManager();

    public final Scanner scanner = new Scanner(System.in);
    private ScannerManager() {}

    public static ScannerManager getInstance(){
        return scannerManager;
    }
}
