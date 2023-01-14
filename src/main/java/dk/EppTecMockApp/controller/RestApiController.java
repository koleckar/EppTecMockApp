package dk.EppTecMockApp.controller;

import dk.EppTecMockApp.dto.CustomerDto;
import dk.EppTecMockApp.model.Customer;
import dk.EppTecMockApp.model.CustomerRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


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
    public ResponseEntity<String> addCustomer(@RequestBody @Valid CustomerDto customerDto) {
        LOG.info("POST " + customerDto);
        customerRepository.save(new Customer(customerDto));
        return ResponseEntity.ok(customerDto + " successfully added.");
    }

    @GetMapping
    public CustomerDto getCustomerByBirthCertificateNumber(@RequestParam("nationalID") String nationalID) {
        LOG.info("POST " + nationalID);
        return new CustomerDto(
                customerRepository.getCustomerByNationalID(nationalID).orElse(new Customer()));
    }

    @DeleteMapping
    public String deleteCustomer(@RequestParam("nationalID") String nationalID) {
        Optional<Customer> retrievedCustomer = customerRepository.getCustomerByNationalID(nationalID);
        if (retrievedCustomer.isPresent()) {
            Customer customer = retrievedCustomer.get();
            customerRepository.deleteById(customer.getId());
            LOG.info("deleted " + new CustomerDto(customer));
            return "Successfully deleted " + new CustomerDto(customer);
        } else {
            LOG.info("failed to delete customer with nationalID" + nationalID);
            return "Failed to delete Customer with nationalID=" + nationalID;
        }
    }

}
