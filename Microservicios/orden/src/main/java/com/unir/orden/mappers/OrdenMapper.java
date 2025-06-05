package com.unir.orden.mappers;

import org.mapstruct.Mapper;

import com.unir.orden.dto.OrdenResponseDTO;
import com.unir.orden.models.Orden;

@Mapper(componentModel = "spring")
public interface OrdenMapper {
    // De entidad a DTO de respuesta
    OrdenResponseDTO toResponseDTO(Orden orden);
}
