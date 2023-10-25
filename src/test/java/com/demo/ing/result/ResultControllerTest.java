package com.demo.ing.result;

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

import static com.demo.ing.TestHelper.asJsonString;
import static com.demo.ing.TestHelper.getResult;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ResultControllerTest {
    @Mock
    private ResultService resultService;
    @InjectMocks
    private ResultController resultController;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(resultController).build();
    }

    @Test
    public void testGetResult() throws Exception {
        Result result = getResult(123L,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(150),
                ResultEnum.STORED_AMOUNT_MORE_THAN_INCOME_AMOUNT);

        when(resultService.getResult(anyLong())).thenReturn(asJsonString(result));

        mockMvc.perform(get("/results/{transactionId}", 123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId", is(result.getTransactionId().intValue())))
                .andExpect(jsonPath("$.destinationId", is(result.getDestinationId().intValue())))
                .andExpect(jsonPath("$.originatorId", is(result.getOriginatorId().intValue())))
                .andExpect(jsonPath("$.storedAmount", is(result.getStoredAmount().intValue())))
                .andExpect(jsonPath("$.incomeAmount", is(result.getIncomeAmount().intValue())))
                .andExpect(jsonPath("$.pdata", is(result.getPdata().intValue())))
                .andExpect(jsonPath("$.resultEnum",
                        is(ResultEnum.STORED_AMOUNT_MORE_THAN_INCOME_AMOUNT.toString())))
                .andExpect(status().isOk());
    }
}
