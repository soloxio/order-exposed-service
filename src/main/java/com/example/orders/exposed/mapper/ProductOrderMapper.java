package com.example.orders.exposed.mapper;

import com.example.orders.exposed.dto.InternalOrderItemRequest;
import com.example.orders.exposed.dto.InternalOrderRequest;
import com.example.orders.tmf622.model.ProductOrderCreate;
import com.example.orders.tmf622.model.ProductOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface ProductOrderMapper {

    @Mapping(target = "items", source = "orderItem")
    InternalOrderRequest toInternal(ProductOrderCreate request);

    @Mapping(target = "lineId", source = "id")
    @Mapping(target = "productOfferingId", source = "productOffering.id")
    @Mapping(target = "productOfferingName", source = "productOffering.name")
    InternalOrderItemRequest toInternalItem(ProductOrderItem item);

    List<InternalOrderItemRequest> map(List<ProductOrderItem> items);
}
