package com.prueba.tecnica.financiero.service.impl;

import com.prueba.tecnica.financiero.dto.ProductoFinancieroDTO;
import com.prueba.tecnica.financiero.dto.ProductoFinancieroMapper;
import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.model.ProductoFinanciero;
import com.prueba.tecnica.financiero.repository.ClienteRepository;
import com.prueba.tecnica.financiero.repository.ProductoFinancieroRepository;
import com.prueba.tecnica.financiero.service.ClienteNegocioService;
import com.prueba.tecnica.financiero.service.ProductoFinancieroNegocioService;
import com.prueba.tecnica.financiero.service.ProductoFinancieroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductoFinancieroServiceImpl implements ProductoFinancieroService {

    private final ProductoFinancieroRepository productoFinancieroRepository;
    private final ProductoFinancieroMapper productoFinancieroMapper;
    private final ClienteNegocioService clienteNegocio;
    private final ProductoFinancieroNegocioService productoFinancieroNegocio;

    @Override
    public List<ProductoFinancieroDTO> buscarTodos() {
        log.debug("buscar todos los productos financieros");
        List<ProductoFinanciero> listProductos = productoFinancieroRepository.findAll();
        log.debug("total productos financieros: {}", listProductos.size());
        List<ProductoFinancieroDTO> listDto = productoFinancieroMapper.toListDto(listProductos);
        log.debug("total productos financieros dto: {}", listDto.size());
        return listDto;
    }

    @Override
    public ProductoFinancieroDTO buscarPorNumeroProducto(BigInteger id) {
        log.debug("consultar producto por numero: {}", id);
        ProductoFinanciero producto = productoFinancieroRepository.findById(id)
                .orElseThrow(() -> {
            log.error("producto no encontrado por numero: {}", id);
            return new ResourceNotFoundException("Producto Financiero", "numero producto", id);
        });
        log.debug("producto financiero encontrado: {}", id);
        return productoFinancieroMapper.toDto(producto);
    }

    @Override
    public ProductoFinancieroDTO crear(ProductoFinancieroDTO dto) {
        log.debug("crear producto: {} - cliente: {}", dto.getTipoProducto(), dto.getIdCliente());
        clienteNegocio.validaSiExisteClientePorId(dto.getIdCliente());
        ProductoFinanciero savedProducto = productoFinancieroRepository.save(productoFinancieroMapper.toEntity(dto));
        log.debug("producto creado numero: {} - cliente: {}", savedProducto.getNumeroProducto(), savedProducto.getCliente().getIdCliente());
        return productoFinancieroMapper.toDto(savedProducto);
    }

    @Override
    public ProductoFinancieroDTO actualizar(ProductoFinancieroDTO dto, BigInteger id) {
        log.debug("actualizar producto: {} - cliente: {}", id, dto.getIdCliente());
        productoFinancieroNegocio.validaSiExisteProductoPorNumero(id);
        clienteNegocio.validaSiExisteClientePorId(dto.getIdCliente());
        ProductoFinanciero producto = productoFinancieroMapper.toEntity(dto);
        producto.setNumeroProducto(id);
        ProductoFinanciero updatedProducto = productoFinancieroRepository.save(producto);
        return productoFinancieroMapper.toDto(updatedProducto);
    }

    @Override
    public void borrarPorNumeroProducto(BigInteger id) {
        if (!productoFinancieroRepository.existsById(id)) {
            throw new ResourceNotFoundException("ProductoFinanciero", "numeroProducto", id);
        }
        productoFinancieroRepository.deleteById(id);
    }
}
