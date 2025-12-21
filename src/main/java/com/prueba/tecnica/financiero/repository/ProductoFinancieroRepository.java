package com.prueba.tecnica.financiero.repository;

import com.prueba.tecnica.financiero.model.ProductoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProductoFinancieroRepository extends JpaRepository<ProductoFinanciero, BigInteger> {
}
