package org.example.utils;

import java.util.Scanner;

/**
 * Singleton class for managing a Scanner object, providing access to a single Scanner instance.
 */
public class ScannerManager {
    private final static ScannerManager scannerManager = new ScannerManager();
    public final Scanner scanner = new Scanner(System.in);
    private ScannerManager() {}

    /**
     * Getting single instance of ScannerManager.
     * @return instance of ScannerManager.
     */
    public static ScannerManager getInstance(){
        return scannerManager;
    }
}
