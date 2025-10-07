package com.example.SWP391_FALL25.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "PartType")
@NoArgsConstructor
@AllArgsConstructor
public class PartType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String partNumber;

    @OneToMany(mappedBy = "partType", cascade = CascadeType.ALL)
    private List<Part> parts;
}
