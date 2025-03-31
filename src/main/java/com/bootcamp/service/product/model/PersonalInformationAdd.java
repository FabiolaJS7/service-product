package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInformationAdd {

    private String name;
    private String lastName;
    private String identification;
    private String typeIdentification;
    private String email;
    private String phone;

}
