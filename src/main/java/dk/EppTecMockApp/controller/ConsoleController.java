package dk.EppTecMockApp.controller;

import dk.EppTecMockApp.dto.CustomerDto;

import dk.EppTecMockApp.model.CustomerRepository;
import dk.EppTecMockApp.service.CustomerService;
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
    private final CustomerService customerService;
    static final Scanner sc = new Scanner(System.in);


    private ConsoleController(CustomerService customerService,
                              CustomerRepository customerRepository) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator(); //validates bean instances
        this.customerService = customerService;
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
        var retrievedCustomer = customerService.getCustomerByNationalID(customerDto.getNationalID());
        retrievedCustomer.ifPresentOrElse(
                existingCustomer -> System.err.println("Customer with nationalID=" + existingCustomer.getNationalID() +
                        " already exists." + " NationalIDs have to be unique."),
                () -> {
                    customerService.saveCustomer(customerDto);
                    System.out.println(customerDto + " successfully added.");
                });

    }

    private void getCustomer() {
        CustomerDto customerDto = new CustomerDto();

        boolean hasNationalIDValidFormat = false;
        while (!hasNationalIDValidFormat) {
            System.out.println("Insert national identification number in the following format YYMMDDXXXX or YYMMDD/XXXX");
            customerDto.setNationalID(sc.next());
            hasNationalIDValidFormat = isValidCustomerProperty(customerDto, "nationalID");
        }

        Optional<CustomerDto> retrievedCustomer = customerService.getCustomerByNationalID(customerDto.getNationalID());

        retrievedCustomer.ifPresentOrElse(
                customer -> {
                    System.out.print(customer);
                    int age = Utils.calculateAgeFromNationalID(customer.getNationalID());
                    String ageString = age >= 0 ? String.valueOf(age) : "Unknown nationalID format, age could not be calculated.";
                    System.out.println(", age=" + ageString);
                },
                () -> System.err.println(
                        "No customer found with nationalID=" + customerDto.getNationalID() + " in the database.")
        );

    }

    private void deleteCustomer() {
        CustomerDto customerDto = new CustomerDto();

        boolean hasNationalIDValidFormat = false;
        while (!hasNationalIDValidFormat) {
            System.out.println("Insert national identification number in the following format YYMMDDXXXX or YYMMDD/XXXX");
            customerDto.setNationalID(sc.next());
            hasNationalIDValidFormat = isValidCustomerProperty(customerDto, "nationalID");
        }

        Optional<CustomerDto> retrievedCustomer = customerService.getCustomerByNationalID(customerDto.getNationalID());
        retrievedCustomer.ifPresentOrElse(
                customer -> {
                    customerService.deleteCustomerByNationalID(customer.getNationalID());
                    System.out.println("Successfully deleted customer: " + customer);
                },
                () -> System.err.println("No customer found with nationalID=" + customerDto.getNationalID() + " in the database."));

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
