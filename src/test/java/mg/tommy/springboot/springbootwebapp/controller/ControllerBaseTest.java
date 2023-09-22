package mg.tommy.springboot.springbootwebapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RequiredArgsConstructor
public abstract class ControllerBaseTest {

    final ObjectMapper objectMapper;

    protected abstract ResultMatcher[] fieldsErrors(int ...errorCount);
    protected abstract String rootPath();
    protected abstract RequestPostProcessor mvcSecurityPostProcessor();
    protected String uuidPath() {
        return rootPath() + "/{uuid}";
    }

    protected RequestBuilder saveRequest(Object requestBody) throws JsonProcessingException {
        return post(rootPath())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .with(mvcSecurityPostProcessor());
    }

    protected RequestBuilder updateRequest(UUID uuid, Object requestBody) throws JsonProcessingException {
        return put(uuidPath(), uuid)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .with(mvcSecurityPostProcessor());
    }

    protected RequestBuilder patchRequest(UUID uuid, Object requestBody) throws JsonProcessingException {
        return patch(uuidPath(), uuid)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .with(mvcSecurityPostProcessor());
    }

}
