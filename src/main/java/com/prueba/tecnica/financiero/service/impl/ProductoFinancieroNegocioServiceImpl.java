package com.prueba.tecnica.financiero.service.impl;

import com.prueba.tecnica.financiero.exception.FondosInsuficientesException;
import com.prueba.tecnica.financiero.exception.NumeroProductoNoEncontradoException;
import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.model.ProductoFinanciero;
import com.prueba.tecnica.financiero.repository.ProductoFinancieroRepository;
import com.prueba.tecnica.financiero.service.ProductoFinancieroNegocioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductoFinancieroNegocioServiceImpl implements ProductoFinancieroNegocioService {

    private final ProductoFinancieroRepository productoFinancieroRepository;

    @Override
    public void validaSiExisteProductoPorNumero(BigInteger numeroProducto) {
        log.info("Validar si existe producto financiero numero: {}", numeroProducto);
        if (!productoFinancieroRepository.existsById(numeroProducto)) {
            log.error("producto financiero no existe: {}", numeroProducto);
            throw new ResourceNotFoundException("ProductoFinanciero", "numeroProducto", numeroProducto);
        }
    }

    @Override
    public ProductoFinanciero buscarProductoPorNumero(BigInteger numeroProducto) {
        log.info("buscar producto financiero numero: {}", numeroProducto);
        return productoFinancieroRepository.findById(numeroProducto)
                .orElseThrow(() -> new NumeroProductoNoEncontradoException("Producto financiero no encontrado, numeroProducto: " + numeroProducto));
    }

    @Override
    public ProductoFinanciero consignar(BigInteger numeroProducto, BigDecimal monto) {
        log.info("consigar en producto financiero numero; {} - monto: {}", numeroProducto, monto);
        ProductoFinanciero cuenta = this.buscarProductoPorNumero(numeroProducto);
        BigDecimal nuevoSaldo = cuenta.getSaldo().add(monto);
        cuenta.setSaldo(nuevoSaldo);

        cuenta = productoFinancieroRepository.save(cuenta);
        log.info("consigar en producto financiero numero; {} - nuevo saldo: {}", numeroProducto, nuevoSaldo);
        return cuenta;
    }

    @Override
    public ProductoFinanciero retirar(BigInteger numeroProducto, BigDecimal monto) {
        log.info("retirar de producto financiero numero: {} - monto: {}", numeroProducto, monto);
        ProductoFinanciero cuenta = this.buscarProductoPorNumero(numeroProducto);

        if(cuenta.getSaldo().compareTo(monto) < 0) {
            throw new FondosInsuficientesException("Fondos insuficientes en la cuenta de origen: " + numeroProducto);
        }

        BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(monto);
        cuenta.setSaldo(nuevoSaldo);

        cuenta = productoFinancieroRepository.save(cuenta);
        log.info("retirar de producto financiero numero: {} - nuevo saldo: {}", numeroProducto, nuevoSaldo);

        return cuenta;
    }

    @Override
    public ProductoFinanciero transferir(BigInteger numeroProductoOrigen, BigInteger numeroProductoDestino, BigDecimal monto) {
        log.info("transferir desde producto numero: {} - hacia producto numero: {} - monto: {}", numeroProductoOrigen, numeroProductoDestino, monto);

        ProductoFinanciero cuentaOrigen = this.buscarProductoPorNumero(numeroProductoOrigen);
        if(cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new FondosInsuficientesException("Fondos insuficientes en la cuenta de origen: " + numeroProductoOrigen);
        }

        ProductoFinanciero cuentaDestino = this.buscarProductoPorNumero(numeroProductoDestino);

        BigDecimal nuevoSaldoOrigen = cuentaOrigen.getSaldo().subtract(monto);
        BigDecimal nuevoSaldoDestino = cuentaDestino.getSaldo().add(monto);

        cuentaOrigen.setSaldo(nuevoSaldoOrigen);
        cuentaDestino.setSaldo(nuevoSaldoDestino);

        ProductoFinanciero cuentaOrigenGuardado = productoFinancieroRepository.save(cuentaOrigen);
        log.info("transferir de producto financiero origen numero: {} - nuevo saldo: {}", cuentaOrigen, nuevoSaldoDestino);

        ProductoFinanciero cuentaDestinoGuardado = productoFinancieroRepository.save(cuentaDestino);
        log.info("transferir de producto financiero destino numero: {} - nuevo saldo: {}", cuentaDestino, cuentaDestinoGuardado.getSaldo());

        return cuentaOrigenGuardado;
    }
}
