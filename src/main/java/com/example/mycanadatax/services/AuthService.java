package com.example.mycanadatax.services;


import com.example.mycanadatax.dto.SignupRequest;
import com.example.mycanadatax.entities.Customer;

public interface AuthService {
    Customer createCustomer(SignupRequest signupRequest);
}
