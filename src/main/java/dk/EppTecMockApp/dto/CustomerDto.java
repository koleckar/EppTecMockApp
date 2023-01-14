package dk.EppTecMockApp.dto;

import dk.EppTecMockApp.model.Customer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import static dk.EppTecMockApp.utils.Constants.*;

@Component
public class CustomerDto {

    @NotNull
    @Size(min = MIN_NAME_LEN, max = MAX_NAME_LEN,
            message = "Name must be between " + MIN_NAME_LEN + " and " + MAX_NAME_LEN + " characters.")
    @Pattern(regexp = "^\\p{Alpha}+$", message = "Only alpha (non-numeric) characters allowed.")
    private String name;
    @NotNull
    @Size(min = MIN_SURNAME_LEN, max = MAX_SURNAME_LEN,
            message = "Surname must be between " + MIN_SURNAME_LEN + " and " + MAX_SURNAME_LEN + " characters.")
    @Pattern(regexp = "^\\p{Alpha}+$", message = "Only alpha (non-numeric) characters allowed.")
    private String surname;

    // TODO? have set of valid nationalID patterns.
    @NotNull
    @Pattern(regexp = "^\\d{6}/*\\d{4}$", message = "Format must be YYMMDDXXXX or YYMMDD/XXXX ")
    private String nationalID;


    public CustomerDto() {
    }

    public CustomerDto(String name, String surname, String nationalID) {
        this.name = name;
        this.surname = surname;
        this.nationalID = nationalID;
    }

    public CustomerDto(Customer customer) {
        setName(customer.getName());
        setSurname(customer.getSurname());
        setNationalID(customer.getNationalID());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID.replace("/", "");
    }


    @Override
    public String toString() {
        return "Customer[" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nationalID=" + nationalID + "]";
    }
}
