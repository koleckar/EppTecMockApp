package dk.EppTecMockApp.controller;

import dk.EppTecMockApp.dto.CustomerDto;

import dk.EppTecMockApp.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



@WebMvcTest(controllers = RestApiController.class)
public class RestApiControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("Should return HttpStatusCode=400.")
    public void testGET_ResponseStatus_InvalidNationalID() throws Exception {
        String invalidNationalID = "0000001111";

        mvc.perform(MockMvcRequestBuilders.get("/customers?nationalID" + invalidNationalID))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

//
//    @Test
//    @DisplayName("Should return Customer with null values and age=-1.")
//    public void testGetResponseBody_NonInvalidNationalID() throws Exception {
//        String invalidNationalID = "0000001111";
//
//        mvc.perform(MockMvcRequestBuilders.get("/customers?nationalID=" + invalidNationalID))
//                .andExpect(MockMvcResultMatchers.status().is(400));
//    }

    @Test
    public void testGET_ResponseStatus_ValidNationalID() throws Exception {
        String validNationalID = "910106/0010";
        String validNationalIDWithoutSlash = "9101060010";

        customerService.saveCustomer(new CustomerDto("Pavel", "Novak", validNationalID));

        mvc.perform(MockMvcRequestBuilders.get("/customers?nationalID=" + validNationalID))
                .andExpect(MockMvcResultMatchers.status().is(200));

        mvc.perform(MockMvcRequestBuilders.get("/customers?nationalID=" + validNationalIDWithoutSlash))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}


