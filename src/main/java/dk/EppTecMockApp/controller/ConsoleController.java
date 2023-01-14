package dk.EppTecMockApp.controller;

import dk.EppTecMockApp.dto.CustomerDto;
import dk.EppTecMockApp.model.Customer;
import dk.EppTecMockApp.model.CustomerRepository;
import dk.EppTecMockApp.utils.Constants;
import dk.EppTecMockApp.utils.Utils;
import jakarta.validation.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.Scanner;


@Component
public class ConsoleController implements CommandLineRunner {
    private final Validator validator;
    private final CustomerRepository customerRepository;
    static final Scanner sc = new Scanner(System.in);

    private ConsoleController(CustomerRepository customerRepository) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator(); //validates bean instances
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        printGreeting();

        while (true) {
            System.out.println("What action would you like to perform? (Type 'help' for available commands)");
            String input = sc.next();

            switch (input) {
                case "help" -> showHelp();
                case "add" -> addCustomer();
                case "get" -> getCustomer();
                case "del" -> deleteCustomer();
                default -> System.err.println("Requested command '" + input + "' is not valid.");
            }
        }
    }


    @Validated
    private void addCustomer() {
        System.out.println("Proceed to create new customer.");
        CustomerDto customerDto = new CustomerDto();

        boolean isNameValid = false;
        while (!isNameValid) {
            System.out.println("Type name: ");
            customerDto.setName(sc.next());
            isNameValid = isValidCustomerProperty(customerDto, "name");
        }

        boolean isSurnameValid = false;
        while (!isSurnameValid) {
            System.out.println("Type surname: ");
            customerDto.setSurname(sc.next());
            isSurnameValid = isValidCustomerProperty(customerDto, "surname");
        }

        boolean isNationalIDValid = false;
        while (!isNationalIDValid) {
            System.out.println("Type national identification number in the following format YYMMDDXXXX or YYMMDD/XXXX: ");
            customerDto.setNationalID(sc.next());
            isNationalIDValid = isValidCustomerProperty(customerDto, "nationalID");
        }

        //todo: let the non-uniques error handle spring?
        Optional<Customer> retrievedCustomer = customerRepository.getCustomerByNationalID(customerDto.getNationalID());
        if (retrievedCustomer.isEmpty()) {
            customerRepository.save(new Customer(customerDto));
            System.out.println(customerDto + " successfully added.");
        } else {
            System.err.println("Customer with nationalID=" + customerDto.getNationalID() + " already exists." +
                    " NationalIDs have to be unique.");
        }

    }

    private void getCustomer() {
        CustomerDto customerDto = new CustomerDto();

        boolean hasNationalIDValidFormat = false;
        while (!hasNationalIDValidFormat) {
            System.out.println("Insert national identification number in the following format YYMMDDXXXX or YYMMDD/XXXX");
            customerDto.setNationalID(sc.next());
            hasNationalIDValidFormat = isValidCustomerProperty(customerDto, "nationalID");
        }

        Optional<Customer> retrievedCustomer = customerRepository.getCustomerByNationalID(customerDto.getNationalID());

        if (retrievedCustomer.isEmpty()) {
            System.err.println("No customer found with nationalID=" + customerDto.getNationalID() + " in the database.");
        } else {
            System.out.print(new CustomerDto(retrievedCustomer.get()));
            int age = Utils.calculateAgeFromNationalID(customerDto.getNationalID());
            String ageString = age >= 0 ? String.valueOf(age) : "Unknown nationalID format, age could not be calculated.";
            System.out.println(", age=" + ageString);
        }

    }

    private void deleteCustomer() {
        CustomerDto customerDto = new CustomerDto();

        boolean hasNationalIDValidFormat = false;
        while (!hasNationalIDValidFormat) {
            System.out.println("Insert national identification number in the following format YYMMDDXXXX or YYMMDD/XXXX");
            customerDto.setNationalID(sc.next());
            hasNationalIDValidFormat = isValidCustomerProperty(customerDto, "nationalID");
        }

        Optional<Customer> retrievedCustomer = customerRepository.getCustomerByNationalID(customerDto.getNationalID());
        if (retrievedCustomer.isEmpty()) {
            System.err.println("No customer found with nationalID=" + customerDto.getNationalID() + " in the database.");
        } else {
            Customer customer = retrievedCustomer.get();
            customerRepository.deleteById(customer.getId());
            System.out.println("Successfully deleted customer: " + new CustomerDto(customer));
        }

    }


    private boolean isValidCustomerProperty(CustomerDto customerDto, String propertyName) {
        var constraintViolations = validator.validateProperty(customerDto, propertyName);
        if (constraintViolations.isEmpty()) {
            return true;
        } else {
            constraintViolations.forEach(
                    constraintViolation -> System.err.println(constraintViolation.getMessage()));
            return false;
        }

    }

    private void printGreeting() {
        System.out.println(Constants.eppTecLogoInAscii);
    }

    private void showHelp() {
        System.out.println("Type: 'add' to add a new customer.");
        System.out.println("Type: 'get' to find an existing customer.");
        System.out.println("Type: 'del' to delete an existing customer.");
    }
}
