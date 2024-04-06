package com.cinema.halls.infrastructure.db;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "hall")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "seats")
public class JpaHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @ElementCollection
    @CollectionTable(name = "seat", joinColumns = @JoinColumn(name = "hall_id"))
    private List<JpaSeat> seats;
}
