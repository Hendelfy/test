package com.example.rbtest.service;

import com.example.rbtest.domain.Document;
import com.example.rbtest.domain.Protocol;
import com.example.rbtest.domain.ProtocolState;
import com.example.rbtest.dto.DocumentDto;
import com.example.rbtest.dto.ProtocolDto;
import com.example.rbtest.repository.DocumentRepository;
import com.example.rbtest.repository.ProtocolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentManageService {
    private final ProtocolRepository protocolRepository;
    private final DocumentRepository documentRepository;

    public DocumentManageService(ProtocolRepository protocolRepository, DocumentRepository documentRepository) {
        this.protocolRepository = protocolRepository;
        this.documentRepository = documentRepository;
    }


    public int createProtocol(ProtocolDto dto) {
        return saveProtocol(dto, new Protocol()).getId();
    }

    private Set<Document> checkIfAllDocumentsPresent(List<Integer> documentIds) {
        Set<Integer> requiredDocumentIds = new HashSet<>(documentIds);
        Set<Document> allDocuments = new HashSet<>(documentRepository.findAllById(requiredDocumentIds));

        requiredDocumentIds.removeAll(allDocuments.stream().map(Document::getId).collect(Collectors.toSet()));
        if (!requiredDocumentIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Documents with id: " + requiredDocumentIds + " not found");
        }
        return allDocuments;
    }

    public void editProtocol(Protocol protocol, ProtocolDto dto) {
        saveProtocol(dto, protocol);
    }

    public Protocol getProtocol(int id) {
        return protocolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Protocol id: " + id + " not found"));
    }

    private Protocol saveProtocol(ProtocolDto dto, Protocol protocol) {
        Set<Document> documents = checkIfAllDocumentsPresent(dto.documentIds());
        protocol.setCreatedAt(dto.createdAt());
        protocol.setCreatedBy(dto.createdBy());
        protocol.setState(dto.state());
        protocol.setDocuments(documents);

        return protocolRepository.save(protocol);
    }

    public void changeProtocolState(Protocol protocol, ProtocolState state) {
        protocol.setState(state);
        protocolRepository.save(protocol);
    }

    public void editDocument(Document document, DocumentDto dto){
        saveDocument(dto, document);
    }

    public void deleteDocumentById(int id) {
        documentRepository.deleteById(id);
    }

    public int createDocument(DocumentDto dto) {
        return saveDocument(dto, new Document()).getId();
    }

    private Document saveDocument(DocumentDto dto, Document document) {
        document.setType(dto.type());
        document.setName(dto.name());
        document.setCreatedAt(dto.createdAt());
        document.setCreatedBy(dto.createdBy());
        return documentRepository.save(document);
    }

    public Document getDocument(int id) {
        return documentRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Document with id " + id + " not found"));
    }
}
