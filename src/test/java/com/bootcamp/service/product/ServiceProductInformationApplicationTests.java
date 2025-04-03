package com.bootcamp.service.product;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.StatusProductConstants;
import com.bootcamp.service.product.model.PassiveProduct;
import com.bootcamp.service.product.model.ProductInformation;
import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.model.SavingAccount;
import com.bootcamp.service.product.util.JsonTransferUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ServiceProductInformationApplicationTests {

	@Disabled
	@Test
	void contextLoads() {
		ProductInformation productInformation = new ProductInformation();

		ProductRequest productRequest = new ProductRequest();
		productRequest.setProductType("SA");

		switch (productRequest.getProductType()) {
			case (ProductTypeConstants.SAVING_ACCOUNT):
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setAccountNumber("12345");
				List<PassiveProduct> listPassiveProduct = new ArrayList<>();
				listPassiveProduct.add(savingAccount);
				System.out.println("probemos");
				break;
			default:
				System.out.println("asdas");
				break;
		}
	}

	@Test
	void shouldCreatePassiveAccount() {
		SavingAccount savingAccount = new SavingAccount();

		System.out.println(JsonTransferUtil.objectToJson(savingAccount));
	}



}
