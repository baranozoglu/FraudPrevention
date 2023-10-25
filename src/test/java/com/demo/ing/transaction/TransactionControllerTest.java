package com.demo.ing.transaction;

import com.demo.ing.transaction.model.IncomeTransaction;
import com.demo.ing.transaction.model.StoredTransaction;
import com.demo.ing.transaction.service.IncomeTransactionService;
import com.demo.ing.transaction.service.StoredTransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.demo.ing.TestHelper.asJsonString;
import static com.demo.ing.TestHelper.getStoredTransaction;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Mock
    private StoredTransactionService storedTransactionService;
    @Mock
    private IncomeTransactionService incomeTransactionService;
    @InjectMocks
    private TransactionController transactionController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testGetStoredTransaction() throws Exception {
        StoredTransaction storedTransaction = getStoredTransaction(123L, BigDecimal.TEN);

        when(storedTransactionService.findByTransactionId(anyLong())).thenReturn(storedTransaction);

        mockMvc.perform(get("/transactions/{transactionId}", 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(storedTransaction.getId().intValue())))
                .andExpect(jsonPath("$.amount", is(storedTransaction.getAmount().intValue())))
                .andExpect(jsonPath("$.metadata.originatorId",
                        is(storedTransaction.getMetadata().getOriginatorId().intValue())))
                .andExpect(jsonPath("$.metadata.destinationId",
                        is(storedTransaction.getMetadata().getDestinationId().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    public void testInsertStoredTransactions() throws Exception {
        List<StoredTransaction> transactions = new ArrayList<>(List.of(getStoredTransaction(123L, BigDecimal.TEN)));

        mockMvc.perform(post("/transactions/stored")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transactions)))
                .andExpect(status().isOk());
    }

    @Test
    public void testPublishIncomeTransactions() throws Exception {
        List<IncomeTransaction> transactions = new ArrayList<>();
        transactions.add(IncomeTransaction.builder()
                .PID(123L)
                .PAMOUNT(BigDecimal.TEN)
                .PDATA(20160101120000L)
                .build());

        doNothing().when(incomeTransactionService).publishIncomeTransaction(anyList());

        mockMvc.perform(post("/transactions/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transactions)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteStoredTransaction() throws Exception {
        mockMvc.perform(delete("/transactions/{transactionId}", 123L))
                .andExpect(status().isOk());
    }


}