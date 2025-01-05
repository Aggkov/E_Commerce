package com.ecommerce.core.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State implements Serializable {
    @Serial
    private static final long serialVersionUID = 5884553238581965017L;
//    private UUID id;
    private String name;

    @Override
    public String toString() {
        return "StateDTO{" +
//                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
