package com.example.demo.converter;

import com.example.demo.dto.requestDTO.OrderRequest;
import com.example.demo.entity.OrderEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {
    @Autowired
    private ModelMapper modelMapper;
    public OrderEntity convertToEntity(OrderRequest orderRequest) {
        return modelMapper.map(orderRequest, OrderEntity.class);
    }
}
