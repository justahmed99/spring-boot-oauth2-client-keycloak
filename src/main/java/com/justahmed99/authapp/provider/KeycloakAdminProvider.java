package com.justahmed99.authapp.provider;

import com.justahmed99.authapp.business.Login;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakAdminProvider implements KeycloakAdminPersister {

  @Value("${keycloak-admin.server-url}")
  private String serverUrl;

  @Value("${keycloak-admin.realm}")
  private String realm;

  @Value("${keycloak-admin.admin-client-id}")
  private String adminClientId;

  @Value("${keycloak-admin.admin-client-secret}")
  private String adminClientSecret;

  @Value("${keycloak-admin.client-id}")
  private String clientId;

  @Value("${keycloak-admin.client-secret}")
  private String clientSecret;

  @Override
  public Boolean createRegularUser(UserRepresentation userRepresentation) {
    Keycloak keycloak = KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .clientId(adminClientId)
        .clientSecret(adminClientSecret)
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
        .build();
    RealmResource realmResource = keycloak.realm(realm);
    UsersResource usersResource = realmResource.users();
    Response response = usersResource.create(userRepresentation);
    return response.getStatus() == 201;
  }

  @Override
  public Map<String, String> login(Login login) {
    Keycloak keycloak = KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .username(login.getUsername())
        .password(login.getPassword())
        .grantType(OAuth2Constants.PASSWORD)
        .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
        .build();

    return Map.of(
        "access_token", keycloak.tokenManager().getAccessToken().getToken(),
        "refresh_token", keycloak.tokenManager().refreshToken().getToken()
    );
  }
}

