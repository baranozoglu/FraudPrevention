package com.demo.ing.transaction.service;

import com.demo.ing.transaction.model.StoredTransaction;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class TransactionPopulateService {
    private static final String QUOTE = "'";
    private static final String WHITESPACE = " ";
    private static final String FILENAME = "populateScript.txt";
    private static final String FILE_NOT_FOUND = "Population script file not found! ";
    private final StoredTransactionService transactionService;

    public TransactionPopulateService(StoredTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private static String[] getSplitStringByQuote(String data) {
        return data.split(QUOTE);
    }

    private static StoredTransaction getStoredTransaction(Gson gson, String[] splitData) {
        return gson.fromJson(splitData[1], StoredTransaction.class);
    }

    public void populateRedis() {
        List<StoredTransaction> transactions = new ArrayList<>();
        try {
            File populationScript = getFile();
            readFromFileAddToTransactionList(transactions, populationScript);
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        if (!transactions.isEmpty()) {
            transactionService.save(transactions);
        }
    }

    private void readFromFileAddToTransactionList(List<StoredTransaction> transactions, File populationScript)
            throws FileNotFoundException {
        Scanner myReader = new Scanner(populationScript);
        Gson gson = new Gson();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] splitData = getSplitStringByQuote(data);
            StoredTransaction transaction = getStoredTransaction(gson, splitData);
            transaction.setId(getId(splitData));
            transactions.add(transaction);
        }
        myReader.close();
    }

    private Long getId(String[] splitData) {
        return Long.valueOf(splitData[0].split(WHITESPACE)[1]);
    }

    private File getFile() throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource(FILENAME);
        if (url == null) {

            throw new IllegalArgumentException(FILE_NOT_FOUND);
        }
        return new File(url.toURI());
    }
}
