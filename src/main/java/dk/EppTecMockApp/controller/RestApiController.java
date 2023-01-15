package dk.EppTecMockApp.controller;

import dk.EppTecMockApp.dto.CustomerDto;

import dk.EppTecMockApp.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(value = "/customers")
public class RestApiController {

    private final Logger LOG = LoggerFactory.getLogger(RestApiController.class);
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody @Valid CustomerDto customerDto) {
        customerService.saveCustomer(customerDto);
        logRequest("POST", customerDto);
        return ResponseEntity.ok(customerDto + " successfully added.");
    }
    // todo:
    //  nationalID jakarta.validation err not propagating.
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCustomer(@RequestParam("nationalID") String nationalID) {
        Optional<CustomerDto> customer = customerService.getCustomerByNationalID(nationalID);
        logRequest("GET", customer, nationalID);
        return customer.map(
                        val -> new ResponseEntity<>(
                                Map.of("customer", val,
                                        "age", customerService.getAge(nationalID)),
                                HttpStatusCode.valueOf(200)))
                .orElse(new ResponseEntity<>(
                        Map.of("customer", new CustomerDto(),
                                "age", "null"),
                        HttpStatusCode.valueOf(400)));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, CustomerDto>> deleteCustomer(@RequestParam("nationalID") String nationalID) {
        Optional<CustomerDto> customer = customerService.getCustomerByNationalID(nationalID);
        customerService.deleteCustomerByNationalID(nationalID);
        logRequest("DEL", customer, nationalID);

        return customer.map(
                        val -> new ResponseEntity<>(
                                Map.of("customer", val),
                                HttpStatusCode.valueOf(200)))
                .orElse(new ResponseEntity<>(
                        Map.of("customer", new CustomerDto()),
                        HttpStatusCode.valueOf(400)));
    }


    private void logRequest(String requestType, Optional<CustomerDto> customer, String nationalID) {
        LOG.info(requestType);
        customer.ifPresentOrElse(
                val -> LOG.info(val + " success."),
                () -> LOG.info("Customer with nationalID=" + nationalID + " not present"));
    }

    private void logRequest(String requestType, CustomerDto customer) {
        LOG.info(requestType + " " + customer);
    }
}
