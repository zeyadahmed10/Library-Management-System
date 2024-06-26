package com.zeyad.maid.lms.services;

import com.zeyad.maid.lms.dto.request.SigninRequestDTO;
import com.zeyad.maid.lms.dto.request.SignupRequestDTO;
import com.zeyad.maid.lms.exceptions.ResourceExistedException;
import com.zeyad.maid.lms.exceptions.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyCloakService {

    private final Keycloak keycloak;

    private final RestTemplate restTemplate;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.loginUrl}")
    private String loginUrl;

    public ResponseEntity<Object> getUserToken(SigninRequestDTO signinRequestDTO) {
        HttpHeaders headers = createHttpHeaders();
        MultiValueMap<String, String> requestBody = createRequestBody(signinRequestDTO);
        HttpEntity<MultiValueMap<String, String>> loginEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(loginUrl, HttpMethod.POST, loginEntity, Object.class);
    }

    public String createUser(SignupRequestDTO signupRequestDTO) {
        var passwordCredentials = createPasswordCredentials(signupRequestDTO);
        var roleRepresentation = getRoleRepresentation(signupRequestDTO.getRole());
        var userRepresentation = getUserRepresentation(signupRequestDTO);

        userRepresentation.setCredentials(Arrays.asList(passwordCredentials));
        Response response = keycloak.realm(realm).users().create(userRepresentation);

        if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            throw new RuntimeException("Error creating user: " + signupRequestDTO + " with code: " + response.getStatus());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        userResource.roles().realmLevel().add(Arrays.asList(roleRepresentation));
        return "User created successfully with Id: " + userId;
    }

    public CredentialRepresentation createPasswordCredentials(SignupRequestDTO signupRequestDTO) {
        if (!signupRequestDTO.getPassword().equals(signupRequestDTO.getConfirmedPassword())) {
            throw new IllegalArgumentException("Confirmed password does not match password");
        }

        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(signupRequestDTO.getPassword());

        return passwordCredentials;
    }

    public RoleRepresentation getRoleRepresentation(String roleName) {
        RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        if (roleRepresentation == null) {
            throw new ResourceNotFoundException("Role " + roleName + " not found");
        }

        return roleRepresentation;
    }

    public UserRepresentation getUserRepresentation(SignupRequestDTO signupRequestDTO) {
        boolean userExists = keycloak.realm(realm).users().searchByUsername(signupRequestDTO.getUsername(), true).size() > 0;
        if (userExists) {
            throw new ResourceExistedException("User already exists with username " + signupRequestDTO.getUsername());
        }

        boolean emailExists = keycloak.realm(realm).users().searchByEmail(signupRequestDTO.getEmail(), true).size() > 0;
        if (emailExists) {
            throw new ResourceExistedException("User already exists with email " + signupRequestDTO.getEmail());
        }

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(signupRequestDTO.getUsername());
        userRepresentation.setEmail(signupRequestDTO.getEmail());
        userRepresentation.setFirstName(signupRequestDTO.getFirstName());
        userRepresentation.setLastName(signupRequestDTO.getLastName());
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        return headers;
    }

    private MultiValueMap<String, String> createRequestBody(SigninRequestDTO signinRequestDTO) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", OAuth2Constants.PASSWORD);
        requestBody.add("username", signinRequestDTO.getUsername());
        requestBody.add("password", signinRequestDTO.getPassword());
        requestBody.add("client_id", clientId);
        return requestBody;
    }
}
