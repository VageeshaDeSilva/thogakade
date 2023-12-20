package dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CustomerTm {
    private String id;
    private String name;
    private String address;
    private double salary;
    private JFXButton btn;
}
