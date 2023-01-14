package dk.EppTecMockApp.repository;

import dk.EppTecMockApp.dto.CustomerDto;
import dk.EppTecMockApp.model.Customer;
import dk.EppTecMockApp.model.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void shouldSaveCustomer() {
        Customer customer = new Customer(new CustomerDto("Pavel", "Novak", "810603/4444"));
        Customer savedCustomer = customerRepository.save(customer);
        Assertions.assertThat(savedCustomer).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(customer);

    }
}
