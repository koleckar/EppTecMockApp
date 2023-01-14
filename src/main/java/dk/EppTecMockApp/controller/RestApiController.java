package dk.EppTecMockApp.controller;

import dk.EppTecMockApp.dto.CustomerDto;
import dk.EppTecMockApp.model.Customer;
import dk.EppTecMockApp.model.CustomerRepository;
import dk.EppTecMockApp.utils.Utils;
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

    private final Logger LOG;
    @Autowired
    private final CustomerRepository customerRepository;


    public RestApiController(CustomerRepository customerRepository) {
        LOG = LoggerFactory.getLogger(RestApiController.class);
        this.customerRepository = customerRepository;
    }


    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody @Valid CustomerDto customerDto) {
        LOG.info("POST " + customerDto);
        customerRepository.save(new Customer(customerDto));
        LOG.info("POST " + customerDto + " successfully added.");
        return ResponseEntity.ok(customerDto + " successfully added.");
    }

    //todo:
    // nationalID jakarta.validation err not propagating.
    @GetMapping
    public ResponseEntity<Object> getCustomerByBirthCertificateNumber(@RequestParam("nationalID") String nationalID) {
        LOG.info("POST " + nationalID);
        Optional<Customer> customer = customerRepository.getCustomerByNationalID(
                nationalID.replace("/", ""));

        if (customer.isPresent()) {
            return new ResponseEntity<>(
                    Map.of("customer", new CustomerDto(customer.get()),
                            "age", Utils.calculateAgeFromNationalID(nationalID)),
                    HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(
                    Map.of("customer", new CustomerDto(),
                            "age", "null"),
                    HttpStatusCode.valueOf(400));
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCustomer(@RequestParam("nationalID") String nationalID) {
        Optional<Customer> retrievedCustomer = customerRepository.getCustomerByNationalID(nationalID.replace("/", ""));
        if (retrievedCustomer.isPresent()) {
            Customer customer = retrievedCustomer.get();
            customerRepository.deleteById(customer.getId());
            LOG.info("deleted " + new CustomerDto(customer));
            return new ResponseEntity<>(
                    "Successfully deleted " + new CustomerDto(customer),
                    HttpStatusCode.valueOf(200));
        } else {
            LOG.info("failed to delete customer with nationalID" + nationalID);
            return new ResponseEntity<>(
                    "Failed to delete Customer with nationalID=" + nationalID,
                    HttpStatusCode.valueOf(400));
        }
    }

}
