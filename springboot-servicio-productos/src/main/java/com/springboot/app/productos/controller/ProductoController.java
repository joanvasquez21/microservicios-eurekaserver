package com.springboot.app.productos.controller;
 
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import  springboot.servicio.commons.models.entity.Producto;
import com.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {

	@Autowired
	private Environment env;
	
	@Autowired
	private IProductoService productoService;
	
	
	@GetMapping("/listar")
	public List<Producto> listar(){
		
		return productoService.findAll().stream().map(producto -> {
			producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
				return producto;
		
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id) throws InterruptedException {
		Producto producto = productoService.findById(id);
		
		if(id.equals(10L)) {
			throw new IllegalStateException("Producto no encontrado");
		}
		if(id.equals(7L)) {
			TimeUnit.SECONDS.sleep(5L);
		}
		producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		//	producto.setPort(port);
		/*
		boolean ok = false;
		if(!ok) {
			throw new RuntimeException("no se pudo cargar el producto");
		}
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
		
		return producto;
	}
	
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return   productoService.save(producto);
	}
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
		Producto productoDB = productoService.findById(id);
		
		productoDB.setNombre(producto.getNombre());
		
		productoDB.setPrecio(producto.getPrecio());
		
		return productoService.save(productoDB);
		
	}
	
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) 
	public void eliminar(@PathVariable Long id) {
		productoService.deleteById(id);
	}
}
