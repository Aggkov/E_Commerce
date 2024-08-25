package com.me.ecommerce.dto.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1443655363200534746L;
    private Long id;

    private String name;
}
