package fun.junjie.ftgo.orderservice.sagaparticipants;

import fun.junjie.ftgo.common.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeginReviseOrderReply {

    private Money revisedOrderTotal;

}
