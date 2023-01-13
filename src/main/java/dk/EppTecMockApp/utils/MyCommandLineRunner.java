package dk.EppTecMockApp.utils;

import dk.EppTecMockApp.dto.CustomerDto;
import dk.EppTecMockApp.model.Customer;
import dk.EppTecMockApp.model.CustomerRepository;
import jakarta.validation.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Scanner;


@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private final Validator validator;
    private final CustomerRepository customerRepository;

    private MyCommandLineRunner(CustomerRepository customerRepository) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator(); //validates bean instances
        this.customerRepository = customerRepository;
    }

    static final Scanner sc = new Scanner(System.in);

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

    //TODO: throw exception if the customer exists.

    @Validated
    private void addCustomer() {
        System.out.println("Proceed to create new customer.");
        CustomerDto customerDto = new CustomerDto();

        boolean namePropertyValid = false;
        while (!namePropertyValid) {
            System.out.println("Type name: ");
            customerDto.setName(sc.next());
            namePropertyValid = isValidCustomerProperty(customerDto, "name");
        }

        boolean surnamePropertyValid = false;
        while (!surnamePropertyValid) {
            System.out.println("Type surname: ");
            customerDto.setSurname(sc.next());
            surnamePropertyValid = isValidCustomerProperty(customerDto, "surname");
        }

        boolean nationalIDPropertyValid = false;
        while (!nationalIDPropertyValid) {
            System.out.println("Type national identification number in the following format YYMMDDXXXX or YYMMDD/XXXX: ");
            customerDto.setNationalID(sc.next());
            nationalIDPropertyValid = isValidCustomerProperty(customerDto, "nationalID");
        }

        //todo: handle existing Customer!
        customerRepository.save(new Customer(customerDto));
        System.out.println(customerDto + " successfully added.");

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

    private void getCustomer() {

        boolean customerNationalIDValid = false;
        while (!customerNationalIDValid) {
            System.out.println("Insert national identification number in the following format YYMMDDXXXX or YYMMDD/XXXX");

        }
    }

    private void deleteCustomer() {
        System.out.println("deleting customer");
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
