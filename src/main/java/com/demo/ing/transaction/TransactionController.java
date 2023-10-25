package com.demo.ing.transaction;

import com.demo.ing.transaction.model.IncomeTransaction;
import com.demo.ing.transaction.model.StoredTransaction;
import com.demo.ing.transaction.service.IncomeTransactionService;
import com.demo.ing.transaction.service.StoredTransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final StoredTransactionService storedTransactionService;
    private final IncomeTransactionService incomeTransactionService;


    public TransactionController(StoredTransactionService storedTransactionService,
                                 IncomeTransactionService incomeTransactionService) {
        this.storedTransactionService = storedTransactionService;
        this.incomeTransactionService = incomeTransactionService;
    }

    @GetMapping("/{transactionId}")
    public StoredTransaction getStoredTransaction(@PathVariable Long transactionId) {
        return storedTransactionService.findByTransactionId(transactionId);
    }

    @PostMapping("/stored")
    public void insertStoredTransactions(@RequestBody List<StoredTransaction> transactions) {
        storedTransactionService.save(transactions);
    }

    @PostMapping("/income")
    public void publishIncomeTransactions(@RequestBody List<IncomeTransaction> transactions) {
        incomeTransactionService.publishIncomeTransaction(transactions);
    }

    @DeleteMapping("/{transactionId}")
    public void deleteStoredTransaction(@PathVariable Long transactionId) {
        storedTransactionService.deleteTransaction(transactionId);
    }

}
