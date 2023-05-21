package com.springboot.app.item.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springboot.app.item.models.Item;
import com.springboot.app.item.models.Producto;
import com.springboot.app.item.models.service.ItemService;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	
	@GetMapping("/listar")
	public List<Item> listar(){
		return itemService.findAll();
		
	}
	
	@HystrixCommand(fallbackMethod="metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}
	
	public Item metodoAlternativo(@PathVariable Long id, @PathVariable Integer cantidad) {
		Item item = new Item();
		Producto producto = new Producto();

		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("camara sony");
		producto.setPrecio(500.00);
		
		item.setProducto(producto);
		return item;
		
	}
	
	
}
