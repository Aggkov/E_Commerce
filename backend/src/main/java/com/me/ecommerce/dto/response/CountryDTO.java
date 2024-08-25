package com.me.ecommerce.dto.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4773536213554180880L;

    private Short id;

    private String code;

    private String name;
}
