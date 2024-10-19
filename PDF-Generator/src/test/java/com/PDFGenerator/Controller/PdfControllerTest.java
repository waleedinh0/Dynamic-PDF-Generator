package com.PDFGenerator.Controller;

import com.PDFGenerator.Model.Item;
import com.PDFGenerator.Model.PdfRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PdfControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPdfGenerationEndpoint() throws Exception {
        PdfRequest request = new PdfRequest();
        request.setSeller("XYZ Pvt. Ltd.");
        request.setSellerGstIn("29AABBCCDD121ZD");
        request.setSellerAddress("New Delhi, India");
        request.setBuyer("ABC Computers");
        request.setBuyerGstIn("29AABBCCDD131ZD");
        request.setBuyerAddress("New Delhi, India");

        Item item = new Item();
        item.setName("Product 1");
        item.setQuantity("12 Nos");
        item.setRate(123.00);
        item.setAmount(1476.00);
        request.setItems(Collections.singletonList(item));

         mockMvc.perform(post("/api/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment;filename=invoice.pdf"));
    }
}
