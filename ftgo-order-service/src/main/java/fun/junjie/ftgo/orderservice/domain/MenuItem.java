package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.common.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    private String id;
    private String name;
    private Money price;

}
