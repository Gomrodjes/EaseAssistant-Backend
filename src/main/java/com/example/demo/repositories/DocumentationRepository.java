package com.example.demo.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Documentation;

@Repository("documentationRepository")
public interface DocumentationRepository extends JpaRepository<Documentation, Serializable>{

}
