package dk.EppTecMockApp.controller;

import dk.EppTecMockApp.dto.CustomerDto;
import dk.EppTecMockApp.model.Customer;
import dk.EppTecMockApp.model.CustomerRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/customers")
public class RestApiController {

    private final Logger LOG;
    private final CustomerRepository customerRepository;


    public RestApiController(CustomerRepository customerRepository) {
        LOG = LoggerFactory.getLogger(RestApiController.class);
        this.customerRepository = customerRepository;
    }


    @PostMapping
    public void addCustomer(@RequestBody @Valid CustomerDto customerDto) {
        customerRepository.save(new Customer(customerDto));
    }

    @GetMapping
    public void getCustomerByBirthCertificateNumber(@RequestParam("nationalID") String nationalID) {
    }

    @DeleteMapping
    public void deleteCustomer(@RequestParam("nationalID") String nationalID) {
    }

}
