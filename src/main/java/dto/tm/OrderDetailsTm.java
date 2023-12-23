package dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailsTm {
    private String OrderId;
    private String  itemCode;
    private int qty;
    private Double unitpice;
}
