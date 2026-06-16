package com.minimarket.service.impl;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.service.ProductoService;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoService productoService;

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta save(Venta venta) {
        if (!tieneStockSuficiente(venta)) { // Se verifica el stock antes de registrar la venta.
            throw new IllegalArgumentException("No es posible registrar la venta porque uno o más productos no tienen stock suficiente."); // Se explica por qué se rechaza la operación.
        }
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public boolean tieneStockSuficiente(Venta venta) {
        if (venta == null || venta.getDetalles() == null || venta.getDetalles().isEmpty()) { // Se valida que la venta tenga detalles para poder revisar stock.
            return false; // Sin detalles no hay una venta procesable.
        }
        for (DetalleVenta detalle : venta.getDetalles()) { // Se recorren uno a uno los productos incluidos en la venta.
            if (detalle == null || detalle.getProducto() == null || detalle.getProducto().getId() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) { // Se valida que cada detalle tenga datos mínimos consistentes.
                return false; // Cualquier detalle incompleto invalida la venta.
            }
            Producto producto = productoService.findById(detalle.getProducto().getId()); // Se consulta el producto actual simulando acceso a la capa de datos.
            if (producto == null || producto.getStock() == null || producto.getStock() < detalle.getCantidad()) { // Se compara el stock disponible con la cantidad solicitada.
                return false; // Si no alcanza el stock, la venta no puede continuar.
            }
        }
        return true; // Si todos los detalles tienen stock suficiente, la venta es válida.
    }

    @Override
    public double calcularTotal(Venta venta) {
        if (venta == null || venta.getDetalles() == null) { // Se controla que la venta tenga estructura válida antes de sumar.
            return 0.0; // Si no hay detalles, el total es cero.
        }
        double total = 0.0; // Se inicializa el acumulador del total de la venta.
        for (DetalleVenta detalle : venta.getDetalles()) { // Se recorren todos los productos vendidos.
            if (detalle == null) { // Se evita procesar referencias vacías.
                continue; // Se salta el detalle nulo y se continúa con el siguiente.
            }
            double precioUnitario = detalle.getPrecio() != null // Se decide si el precio viene precargado en el detalle.
                    ? detalle.getPrecio() // Se usa el precio del detalle cuando ya existe.
                    : (detalle.getProducto() != null && detalle.getProducto().getPrecio() != null ? detalle.getProducto().getPrecio() : 0.0); // Si no existe, se usa el precio del producto como respaldo.
            int cantidad = detalle.getCantidad() != null ? detalle.getCantidad() : 0; // Se obtiene la cantidad vendida o cero si falta.
            total = total + (precioUnitario * cantidad); // Se suma el subtotal de cada línea al total general.
        }
        return total; // Se devuelve el total calculado para la venta.
    }
}
