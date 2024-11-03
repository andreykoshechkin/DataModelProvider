package org.example.datamodelprovider.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "deposit")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dtopen", nullable = false)
    private LocalDate dtOpen;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "servicetype", nullable = false, length = 1)
    private String serviceType;

    @Column(name = "requesttype", nullable = false, length = 1)
    private String requestType;

    @Column(nullable = false, unique = true, length = 255)
    private String email;
}