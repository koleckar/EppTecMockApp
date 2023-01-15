package dk.EppTecMockApp.service;

import dk.EppTecMockApp.dto.CustomerDto;
import dk.EppTecMockApp.model.Customer;
import dk.EppTecMockApp.model.CustomerRepository;
import dk.EppTecMockApp.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(CustomerDto customerDto) {
        customerRepository.save(new Customer(customerDto));
    }

    public Optional<CustomerDto> getCustomerByNationalID(String nationalID) {
        nationalID = nationalID.replace("/", "");
        return customerRepository.getCustomerByNationalID(nationalID)
                .map(CustomerDto::new);
    }

    public void deleteCustomerByNationalID(String nationalID) {
        nationalID = nationalID.replace("/", "");
        Optional<Customer> retrievedCustomer = customerRepository.getCustomerByNationalID(nationalID);
        retrievedCustomer.ifPresent(customer -> customerRepository.deleteById(customer.getId()));
    }

    public int getAge(String nationalID) {
        return Utils.calculateAgeFromNationalID(nationalID);
    }
}
