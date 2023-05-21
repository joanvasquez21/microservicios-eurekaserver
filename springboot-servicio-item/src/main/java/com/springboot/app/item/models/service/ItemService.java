package com.springboot.app.item.models.service;

import java.util.List;

import com.springboot.app.item.models.Item;

//Servicio porque necesitamos comunicarnos con el microservicio productos para obtener datos
public interface ItemService {
	
	public List<Item> findAll();
	
	public Item findById(Long id, Integer cantidad);
}
