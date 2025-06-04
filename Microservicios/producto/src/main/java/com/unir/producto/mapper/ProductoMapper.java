package com.unir.producto.mapper;

import com.unir.producto.dto.ProductoRequestDTO;
import com.unir.producto.dto.ProductoResponseDTO;
import com.unir.producto.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    @Mapping(target = "id", ignore = true)
    Producto requestToProducto(ProductoRequestDTO request);

    ProductoResponseDTO productoToResponse(Producto producto);
}