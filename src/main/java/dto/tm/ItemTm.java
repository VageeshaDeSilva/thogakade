package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter

public class ItemTm extends RecursiveTreeObject<ItemTm> {
    private String code;
    private String description;
    private double unitPrice;
    private int qty;
    private JFXButton btn;

    public ItemTm(String itemCode, String description, double unitPrice, int qty) {
        this.code = itemCode;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }
}


