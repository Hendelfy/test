package com.example.rbtest;

import com.example.rbtest.domain.Document;
import com.example.rbtest.domain.DocumentType;
import com.example.rbtest.domain.Protocol;
import com.example.rbtest.domain.ProtocolState;
import com.example.rbtest.dto.CreatedId;
import com.example.rbtest.dto.DocumentDto;
import com.example.rbtest.dto.ProtocolDto;
import com.example.rbtest.dto.ProtocolStateDto;
import com.example.rbtest.repository.DocumentRepository;
import com.example.rbtest.repository.ProtocolRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ManageControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    ProtocolRepository protocolRepository;

    @Value("${spring.security.user.name}")
    String user;
    @Value("${spring.security.user.password}")
    String password;

    @Test
    public void testCreateDocument() throws Exception {
        DocumentDto document = new DocumentDto(LocalDateTime.now(), "Creator Name", "Document name", DocumentType.JPG);
        String mvcResult = mockMvc.perform(getWith(MockMvcRequestBuilders.post("/document"), document)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(httpBasic(user, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        int id = objectMapper.readValue(mvcResult, CreatedId.class).id();
        Assertions.assertTrue(documentRepository.existsById(id));
    }

    @Test
    public void testEditDocument() throws Exception {
        Document document = createDocument();
        DocumentDto documentDto = new DocumentDto(LocalDateTime.now(), "Creator Name", "Updater", DocumentType.JPG);
        mockMvc.perform(getWith(MockMvcRequestBuilders.put("/document/" + document.getId()), documentDto))
                .andExpect(status().isOk());
        Document updatedDocument = documentRepository.getReferenceById(document.getId());
        Assertions.assertEquals(documentDto.name(), updatedDocument.getName());
    }

    private MockHttpServletRequestBuilder getWith(MockHttpServletRequestBuilder builder, Object body) throws JsonProcessingException {
        return builder
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(user, password));
    }

    @Test
    public void testDeleteDocument() throws Exception {
        Document document = createDocument();
        Optional<Document> deletedDocument = documentRepository.findById(document.getId());
        Assertions.assertTrue(deletedDocument.isPresent());

        mockMvc.perform(getWith(MockMvcRequestBuilders.delete("/document/" + document.getId()), null)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        deletedDocument = documentRepository.findById(document.getId());
        Assertions.assertFalse(deletedDocument.isPresent());
    }

    @Test
    public void testCreateProtocol() throws Exception {
        Document document = createDocument();
        Document document2 = createDocument();
        ProtocolDto protocolDto = new ProtocolDto(
                LocalDateTime.now(),
                "Creator Name",
                ProtocolState.NEW,
                List.of(document.getId(), document2.getId())
        );

        String mvcResult = mockMvc.perform(getWith(MockMvcRequestBuilders.post("/protocol"), protocolDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        int id = objectMapper.readValue(mvcResult, CreatedId.class).id();
        Protocol protocol = protocolRepository.getReferenceById(id);
        Assertions.assertEquals(2, protocol.getDocuments().size());
        Assertions.assertTrue(protocol.getDocuments().contains(document));
        Assertions.assertTrue(protocol.getDocuments().contains(document2));
    }

    @Test
    public void testEditProtocol() throws Exception {
        Protocol protocol = createProtocol();
        Assertions.assertEquals(2, protocol.getDocuments().size());

        ProtocolDto protocolDto = new ProtocolDto(
                LocalDateTime.now(),
                "Creator Name",
                ProtocolState.CANCELLED,
                Collections.singletonList(protocol.getDocuments().stream().findFirst().get().getId())
        );
        mockMvc.perform(getWith(MockMvcRequestBuilders.put("/protocol/" + protocol.getId()), protocolDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        protocol = protocolRepository.getReferenceById(protocol.getId());

        Assertions.assertEquals(1, protocol.getDocuments().size());
        Assertions.assertEquals(ProtocolState.CANCELLED, protocol.getState());
    }

    @Test
    public void testUpdateStateProtocol() throws Exception {
        Protocol protocol = createProtocol();

        ProtocolState state = ProtocolState.CANCELLED;
        Assertions.assertNotEquals(state, protocol.getState());

        mockMvc.perform(getWith(MockMvcRequestBuilders.patch("/protocol/" + protocol.getId()), new ProtocolStateDto(state))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        protocol = protocolRepository.getReferenceById(protocol.getId());
        Assertions.assertEquals(state, protocol.getState());
    }

    private Protocol createProtocol() {
        Protocol protocol = new Protocol();
        protocol.setState(ProtocolState.NEW);
        protocol.setCreatedBy("Creator");
        protocol.setCreatedAt(LocalDateTime.now());
        protocol.setDocuments(new HashSet<>(List.of(createDocument(), createDocument())));
        return protocolRepository.save(protocol);
    }

    private Document createDocument() {
        Document document = new Document();
        document.setType(DocumentType.JPG);
        document.setName("Document1");
        document.setCreatedBy("Creator");
        document.setCreatedAt(LocalDateTime.now());
        return documentRepository.save(document);
    }
}
