package com.springboot.app.productos.models.dao;

import org.springframework.data.repository.CrudRepository;

import  springboot.servicio.commons.models.entity.Producto;

public interface ProductoDAO extends CrudRepository<Producto, Long>  {

}
