package com.example.application.repository;

import com.example.application.data.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyReposiotry extends JpaRepository<Company,Integer> {
}
