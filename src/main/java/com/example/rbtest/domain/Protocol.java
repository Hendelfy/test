package com.example.rbtest.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(schema = "public", name = "protocol")
public class Protocol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdAt;
    private String createdBy;
    @Enumerated(EnumType.STRING)
    private ProtocolState state;
    @OneToMany
    @JoinColumn(name = "protocol_id")
    private Set<Document> documents = new HashSet<>();
}

