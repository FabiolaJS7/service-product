package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.PlasticCard;
import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.repository.PlasticCardRepository;
import com.bootcamp.service.product.service.PlasticCardService;
import com.bootcamp.service.product.util.NumberRandomUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class PlasticCardServiceImpl implements PlasticCardService {

    PlasticCardRepository plasticCardRepository;

    @Override
    public Mono<String> createPlasticCard(Product product) {
        log.info("Init creating plasticCard");
        PlasticCard plasticCard = new PlasticCard();
        plasticCard.setProductId(product.getId());
        plasticCard.setNumber(NumberRandomUtil.generateNumberCreditCard().block());
        plasticCard.setExpirationDate("05/2029");
        plasticCard.setType(ProductTypeConstants.PASSIVE_PRODUCTS.contains(product.getProductType()) ? "DEBIT" : "'CREDIT'");

        return plasticCardRepository.save(plasticCard)
                .flatMap(p -> Mono.just(p.getId()))
                .doOnNext(id -> log.info("Plastic card created: {}", id))
                .doOnSuccess(id -> log.info("End plastic card created."));

    }
}
