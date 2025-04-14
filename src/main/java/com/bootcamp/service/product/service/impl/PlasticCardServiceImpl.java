package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.constants.PlasticCardTypeConstants;
import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.mapper.PlasticCardMapper;
import com.bootcamp.service.product.model.PlasticCard;
import com.bootcamp.service.product.model.PlasticCardBean;
import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.repository.DaoPlasticCardFactory;
import com.bootcamp.service.product.service.PlasticCardService;
import com.bootcamp.service.product.util.JsonTransferUtil;
import com.bootcamp.service.product.util.NumberRandomUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class PlasticCardServiceImpl implements PlasticCardService {

    @Autowired
    DaoPlasticCardFactory daoPlasticCardFactory;

    @Override
    public Mono<String> createPlasticCard(Product product) {
        log.info("Init creating plasticCard");
        PlasticCard plasticCard = new PlasticCard();
        plasticCard.setProductId(product.getId());
        plasticCard.setNumber(NumberRandomUtil.generateNumberCreditCard().block());
        plasticCard.setExpirationDate("05/2029");
        plasticCard.setType(ProductTypeConstants.PASSIVE_PRODUCTS.contains(product.getProductType())
                ? PlasticCardTypeConstants.DEBIT : PlasticCardTypeConstants.CREDIT);

        return daoPlasticCardFactory.getPlasticCardRepository().save(plasticCard)
                .flatMap(p -> Mono.just(p.getId()))
                .doOnNext(id -> log.info("Plastic card created: {}", id))
                .doOnSuccess(id -> log.info("End plastic card created."));

    }

    @Override
    public Mono<PlasticCardBean> getPlasticCardById(String cardId) {
        log.info("Init getPlasticCardByNumber {}", cardId);
        return daoPlasticCardFactory.getPlasticCardRepository().findPlasticCardById(cardId)
                .map(PlasticCardMapper.INSTANCE::getPlasticCardBeanByPlasticCard)
                .doOnNext(rs -> log.info("Getting plastic card: {}", JsonTransferUtil.objectToJson(rs)))
                .doOnSuccess(rs -> log.info("End plastic card created to passive account."))
                .doOnError(throwable -> log.error("Error while getting plastic card", throwable));
    }
}
