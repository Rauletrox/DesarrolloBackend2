package com.minimarket.config;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            CarritoRepository carritoRepository,
            InventarioRepository inventarioRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Rol rol = new Rol();
                        rol.setNombre("ROLE_ADMIN");
                        return rolRepository.save(rol);
                    });

            Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                    .orElseGet(() -> {
                        Rol rol = new Rol();
                        rol.setNombre("ROLE_CLIENTE");
                        return rolRepository.save(rol);
                    });

            Usuario admin = usuarioRepository.findByUsername("admin")
                    .orElseGet(() -> {
                        Usuario usuario = new Usuario();
                        usuario.setUsername("admin");
                        usuario.setPassword(passwordEncoder.encode("admin123"));
                        Set<Rol> roles = new LinkedHashSet<>();
                        roles.add(rolAdmin);
                        usuario.setRoles(roles);
                        return usuarioRepository.save(usuario);
                    });

            Usuario cliente = usuarioRepository.findByUsername("cliente")
                    .orElseGet(() -> {
                        Usuario usuario = new Usuario();
                        usuario.setUsername("cliente");
                        usuario.setPassword(passwordEncoder.encode("cliente123"));
                        Set<Rol> roles = new LinkedHashSet<>();
                        roles.add(rolCliente);
                        usuario.setRoles(roles);
                        return usuarioRepository.save(usuario);
                    });

            Categoria lacteos = categoriaRepository.findAll().stream()
                    .filter(c -> "Lacteos".equalsIgnoreCase(c.getNombre()))
                    .findFirst()
                    .orElseGet(() -> {
                        Categoria categoria = new Categoria();
                        categoria.setNombre("Lacteos");
                        return categoriaRepository.save(categoria);
                    });

            Categoria abarrotes = categoriaRepository.findAll().stream()
                    .filter(c -> "Abarrotes".equalsIgnoreCase(c.getNombre()))
                    .findFirst()
                    .orElseGet(() -> {
                        Categoria categoria = new Categoria();
                        categoria.setNombre("Abarrotes");
                        return categoriaRepository.save(categoria);
                    });

            Producto leche = productoRepository.findAll().stream()
                    .filter(p -> "Leche Entera".equalsIgnoreCase(p.getNombre()))
                    .findFirst()
                    .orElseGet(() -> {
                        Producto producto = new Producto();
                        producto.setNombre("Leche Entera");
                        producto.setPrecio(1290.0);
                        producto.setStock(25);
                        producto.setCategoria(lacteos);
                        return productoRepository.save(producto);
                    });

            Producto arroz = productoRepository.findAll().stream()
                    .filter(p -> "Arroz 1kg".equalsIgnoreCase(p.getNombre()))
                    .findFirst()
                    .orElseGet(() -> {
                        Producto producto = new Producto();
                        producto.setNombre("Arroz 1kg");
                        producto.setPrecio(1390.0);
                        producto.setStock(40);
                        producto.setCategoria(abarrotes);
                        return productoRepository.save(producto);
                    });

            if (carritoRepository.count() == 0) {
                Carrito item1 = new Carrito();
                item1.setUsuario(cliente);
                item1.setProducto(leche);
                item1.setCantidad(2);
                carritoRepository.save(item1);

                Carrito item2 = new Carrito();
                item2.setUsuario(cliente);
                item2.setProducto(arroz);
                item2.setCantidad(1);
                carritoRepository.save(item2);
            }

            if (inventarioRepository.count() == 0) {
                Inventario entradaLeche = new Inventario();
                entradaLeche.setProducto(leche);
                entradaLeche.setCantidad(20);
                entradaLeche.setTipoMovimiento("Entrada");
                entradaLeche.setFechaMovimiento(new Date());
                inventarioRepository.save(entradaLeche);

                Inventario salidaArroz = new Inventario();
                salidaArroz.setProducto(arroz);
                salidaArroz.setCantidad(5);
                salidaArroz.setTipoMovimiento("Salida");
                salidaArroz.setFechaMovimiento(new Date());
                inventarioRepository.save(salidaArroz);
            }
        };
    }
}
