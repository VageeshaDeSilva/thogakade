package dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class OrderTmNew {
    private String id;
    private String date;
    private String customerId;
}
