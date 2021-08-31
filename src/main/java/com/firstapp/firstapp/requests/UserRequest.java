package com.firstapp.firstapp.requests;

import javax.validation.constraints.*;
import java.util.List;

public class UserRequest
{
    /* Attribute */

    @NotBlank(message = "Ce champ est not null !")
    @Size(min = 3, message = "Ce champ doit contenir au moins 5 caractères !")
    private String firstName;

    @NotNull(message = "Ce champ est not null !")
    @Size(min = 3, message = "Ce champ doit contenir au moins 3 caractères !")
    private String lastName;

    @NotNull(message = "Ce champ est not null")
    @Email
    private String email;

    @NotNull(message = "Ce champ est not null")
    @Size(min = 8, message = "Ce champ doit contenir au moins 8 caractères !")
    @Size(max = 12, message = "Ce champ doit contenir au moins 12 caractères !")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Merci de repecter le format password !")
    private String password;

    private List<AddressRequest> addresses;
    
    private ContactRequest contact;



	/* Getters & Setters */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AddressRequest> getAddresses()
    { return addresses; }

    public void setAdsresses(List<AddressRequest> addresses)
    { this.addresses = addresses; }
    
    public ContactRequest getContact() {
		return contact;
	}

	public void setContact(ContactRequest contact) {
		this.contact = contact;
	}
}
