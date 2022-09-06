package com.example.rbtest.controller;

import com.example.rbtest.domain.Document;
import com.example.rbtest.domain.Protocol;
import com.example.rbtest.dto.CreatedId;
import com.example.rbtest.dto.DocumentDto;
import com.example.rbtest.dto.ProtocolDto;
import com.example.rbtest.dto.ProtocolStateDto;
import com.example.rbtest.service.DocumentManageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManageController {
    private final DocumentManageService documentManageService;

    public ManageController(DocumentManageService documentManageService) {
        this.documentManageService = documentManageService;
    }

    @PostMapping("document")
    public CreatedId createDocument(@Validated @RequestBody DocumentDto dto) {
        return new CreatedId(documentManageService.createDocument(dto));
    }

    @PutMapping("document/{id}")
    public void editDocument(@PathVariable int id, @RequestBody DocumentDto dto) {
        Document document = documentManageService.getDocument(id);
        documentManageService.editDocument(document, dto);
    }

    @DeleteMapping("document/{id}")
    public void deleteDocument(@PathVariable int id) {
        documentManageService.getDocument(id);
        documentManageService.deleteDocumentById(id);
    }

    @PostMapping("protocol")
    public CreatedId createProtocol(@RequestBody ProtocolDto dto) {
        return new CreatedId(documentManageService.createProtocol(dto));
    }

    @PutMapping("protocol/{id}")
    public void editProtocol(@PathVariable int id, @RequestBody ProtocolDto dto) {
        Protocol protocol = documentManageService.getProtocol(id);
        documentManageService.editProtocol(protocol, dto);
    }

    @PatchMapping("protocol/{id}")
    public void changeProtocolState(@PathVariable int id, @RequestBody ProtocolStateDto state) {
        Protocol protocol = documentManageService.getProtocol(id);
        documentManageService.changeProtocolState(protocol, state.state());
    }
}
