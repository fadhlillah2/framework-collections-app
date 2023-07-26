package com.kbni.backendcode.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "swapis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Swapis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String gender;

    @Column
    private String homeWorld;
}
