package dk.EppTecMockApp.model;

import dk.EppTecMockApp.dto.CustomerDto;
import jakarta.persistence.*;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "customer_id", nullable = false)
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String nationalID;


    public Customer() {
    }

    public Customer(CustomerDto customerDto) {
        setName(customerDto.getName());
        setSurname(customerDto.getSurname());
        setNationalID(customerDto.getNationalID());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.nationalID = nationalID.replace("\\", "");
    }


    @Override
    public String toString() {
        return "Customer[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nationalID=" + nationalID + ']';
    }

}
