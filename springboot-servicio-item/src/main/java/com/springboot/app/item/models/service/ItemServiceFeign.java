package com.springboot.app.item.models.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.springboot.app.item.clientes.ProductoClienteRest;
import com.springboot.app.item.models.Item;

@Service
@Primary
public class ItemServiceFeign implements ItemService{

	@Autowired
	private ProductoClienteRest clienteFeign;
	
	@Override
	public List<Item> findAll() {
		// Listar retorna una lista de PRODUCTO y necesitamos una lista de ITEM
		return clienteFeign.listar().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		
		return new Item(clienteFeign.detalle(id), cantidad);
	}

	

}
