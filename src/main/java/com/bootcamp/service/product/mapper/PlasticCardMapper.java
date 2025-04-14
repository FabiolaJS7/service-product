package com.bootcamp.service.product.mapper;

import com.bootcamp.service.product.model.PlasticCard;
import com.bootcamp.service.product.model.PlasticCardBean;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface PlasticCardMapper {

    PlasticCardMapper INSTANCE = Mappers.getMapper(PlasticCardMapper.class);

    @Mapping(source = "productId", target = "productIdAssociated")
    @Mapping(source = "number", target = "cardNumber")
    @Mapping(source = "type", target = "cardType")
    PlasticCardBean getPlasticCardBeanByPlasticCard (PlasticCard plasticCard);

}
