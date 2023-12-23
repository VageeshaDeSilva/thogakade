package dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
@NoArgsConstructor
public class OrderDtoNew {
    private String orderId;
    private String date;
    private String customerId;
}
