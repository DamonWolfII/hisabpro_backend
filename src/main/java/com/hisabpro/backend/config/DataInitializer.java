package com.hisabpro.backend.config;

import com.hisabpro.backend.entity.Company;
import com.hisabpro.backend.entity.User;
import com.hisabpro.backend.repository.CompanyRepository;
import com.hisabpro.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            CompanyRepository companyRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.existsByEmail("admin@hisabpro.com")) {
            return;
        }

        Company company = new Company();
        company.setName("HisabPro Demo");
        company.setEmail("company@hisabpro.com");

        company = companyRepository.save(company);

        User admin = new User();

        admin.setName("Admin");
        admin.setEmail("admin@hisabpro.com");

        admin.setPassword(
                passwordEncoder.encode("Admin@123")
        );

        admin.setRole(User.Role.ADMIN);
        admin.setActive(true);
        admin.setCompany(company);

        userRepository.save(admin);

        System.out.println("======================================");
        System.out.println("Initial ADMIN user created");
        System.out.println("Email: admin@hisabpro.com");
        System.out.println("Password: Admin@123");
        System.out.println("======================================");
    }
}