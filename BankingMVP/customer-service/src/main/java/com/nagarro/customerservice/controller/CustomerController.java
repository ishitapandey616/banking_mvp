package com.nagarro.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.nagarro.customerservice.model.Customer;
import com.nagarro.customerservice.service.CustomerService;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }
    
    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.addCustomer(customer);
        return newCustomer;
    }

    @PutMapping("/{customerId}")
    public Customer updateCustomer(@PathVariable Long customerId, @RequestBody Customer updatedCustomer) {
        return customerService.updateCustomer(customerId, updatedCustomer);
    }

    
    
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        boolean result = customerService.deleteCustomer(customerId);
        if(result) {
            // Cascade deletion to Account Management Service
            WebClient webClient = webClientBuilder.baseUrl("http://account-management-service").build();
            webClient.delete()
                    .uri("/accounts/customer/{customerId}", customerId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return ResponseEntity.ok("Customer deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    
    @GetMapping("/{customerId}/validate")
    public ResponseEntity<Boolean> validateCustomer(@PathVariable Long customerId) {
        // Retrieve the customer from the database by customerId
        Customer customer = customerService.getCustomerById(customerId);
        
        // Check if the customer exists
        if (customer != null) {
            // Customer exists, return true to indicate valid customer details
            return ResponseEntity.ok(true);
        } else {
            // Customer does not exist, return false to indicate invalid customer details
            return ResponseEntity.ok(false);
        }
    }
}



