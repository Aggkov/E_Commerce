package com.ecommerce.notification.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State implements Serializable {
    @Serial
    private static final long serialVersionUID = -2608738780065416834L;
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
