package com.example.employee.models;

import javax.persistence.*;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @SequenceGenerator(
            name = "address_sequence",
            sequenceName = "address_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "address_sequence"
    )
    private Long addressId;
    @Column
    private String address;
    @Column
    private String city;
    @Column
    private String state;
    @Column
    private String pincode;
    @Column
    private boolean isActive;
    @Column
    private boolean isDeleted;

    public Address() {
    }

    public Address(String address, String city, String state, String pincode) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.isActive = true;
        this.isDeleted = false;
    }

    public Long getAddressId() {
        return addressId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode='" + pincode + '\'' +
                ", isActive=" + isActive +
                ", isDeleted=" + isDeleted +
                '}';
    }

}
