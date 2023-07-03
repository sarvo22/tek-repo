package com.tekfilo.inventory.stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StockFilterDto implements Serializable {
    private List<Integer> shapeIds;
    private List<Integer> colorIds;
    private List<Integer> cutIds;
    private List<Integer> clarityIds;
}
