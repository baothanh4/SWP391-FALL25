package com.example.SWP391_FALL25.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Part")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "part_type_id")
    private PartType partType;
}
