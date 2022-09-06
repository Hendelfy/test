package com.example.rbtest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(schema = "public", name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdAt;
    private String createdBy;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType type;
    @ManyToOne(fetch = FetchType.LAZY)
    private Protocol protocol;
}
