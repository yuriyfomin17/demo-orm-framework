package org.nimofy.model;


import lombok.NoArgsConstructor;
import lombok.ToString;
import org.nimofy.annotations.Column;
import org.nimofy.annotations.Id;
import org.nimofy.annotations.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "products")
@ToString
public class Product {
    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "producer")
    private String producer;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
}
