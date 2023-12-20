package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class CustomerDto {
    private String id;
    private String name;
    private String address;
    private double salary;
}
