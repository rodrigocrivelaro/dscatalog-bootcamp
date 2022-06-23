package com.devsuperior.dscatalog.entities;

import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    private Long id;
    private String name;
}
