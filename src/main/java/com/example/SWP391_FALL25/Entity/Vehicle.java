package com.example.SWP391_FALL25.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Vehicle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vin;
    private String licensePlate;
    private String brand;
    private String model;


    @Column(name = "purchaseDate")
    private LocalDate purchaseDate;

    private Integer odometer;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = true)
    @JsonBackReference
    private Users customer;

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonIgnore
    private ServiceAppointment appointment;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reminder> reminders;


}
