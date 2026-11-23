package com.h4ckl07d.ledgerlite.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h4ckl07d.ledgerlite.auth.dto.LoginRequest;
import com.h4ckl07d.ledgerlite.auth.dto.LoginResponse;
import com.h4ckl07d.ledgerlite.TestcontainersConfiguration;
import com.h4ckl07d.ledgerlite.user.dto.request.CreateUserRequest;
import com.h4ckl07d.ledgerlite.user.dto.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end authentication tests.
 *
 * Boots the full Spring context backed by a Testcontainers PostgreSQL instance,
 * and exercises the real HTTP endpoints via MockMvc (servlet-level, no mocking).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------
    //  Constants
    // -------------------------------------------------------
    private static final String REGISTER_URL = "/api/v1/auth/register";
    private static final String LOGIN_URL = "/api/v1/auth/login";

    // -------------------------------------------------------
    //  Registration Tests
    // -------------------------------------------------------
    @Nested
    @DisplayName("POST /api/v1/auth/register")
    class RegisterTests {

        @Test
        @DisplayName("should register a new user and return 201 with user details")
        void registerUser_success() throws Exception {
            CreateUserRequest request = new CreateUserRequest(
                    "Alice Smith",
                    "alice@example.com",
                    "securePass123"
            );

            MvcResult result = mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Alice Smith"))
                    .andExpect(jsonPath("$.email").value("alice@example.com"))
                    .andExpect(jsonPath("$.role").value("USER"))
                    .andExpect(jsonPath("$.created_At").exists())
                    .andReturn();

            // Verify response can be deserialized
            UserResponse response = objectMapper.readValue(
                    result.getResponse().getContentAsString(), UserResponse.class);
            assertThat(response.id()).isNotNull();
            assertThat(response.email()).isEqualTo("alice@example.com");
        }

        @Test
        @DisplayName("should return 400/409 when registering with a duplicate email")
        void registerUser_duplicateEmail() throws Exception {
            CreateUserRequest request = new CreateUserRequest(
                    "Bob Jones",
                    "duplicate@example.com",
                    "securePass123"
            );

            // First registration succeeds
            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            // Second registration with same email should fail
            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        @DisplayName("should return 400 when email is blank")
        void registerUser_blankEmail() throws Exception {
            CreateUserRequest request = new CreateUserRequest(
                    "No Email User",
                    "",
                    "securePass123"
            );

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when name is blank")
        void registerUser_blankName() throws Exception {
            CreateUserRequest request = new CreateUserRequest(
                    "",
                    "noname@example.com",
                    "securePass123"
            );

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when password is blank")
        void registerUser_blankPassword() throws Exception {
            CreateUserRequest request = new CreateUserRequest(
                    "No Pass User",
                    "nopass@example.com",
                    ""
            );

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when email format is invalid")
        void registerUser_invalidEmail() throws Exception {
            CreateUserRequest request = new CreateUserRequest(
                    "Bad Email User",
                    "not-an-email",
                    "securePass123"
            );

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when request body is empty")
        void registerUser_emptyBody() throws Exception {
            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    // -------------------------------------------------------
    //  Login Tests
    // -------------------------------------------------------
    @Nested
    @DisplayName("POST /api/v1/auth/login")
    class LoginTests {

        @Test
        @DisplayName("should login successfully and return JWT token")
        void login_success() throws Exception {
            // First, register the user
            CreateUserRequest registerReq = new CreateUserRequest(
                    "Login User",
                    "loginuser@example.com",
                    "securePass123"
            );

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerReq)))
                    .andExpect(status().isCreated());

            // Now login
            LoginRequest loginReq = new LoginRequest(
                    "loginuser@example.com",
                    "securePass123"
            );

            MvcResult result = mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginReq)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.email").value("loginuser@example.com"))
                    .andReturn();

            LoginResponse response = objectMapper.readValue(
                    result.getResponse().getContentAsString(), LoginResponse.class);
            assertThat(response.token()).isNotBlank();
            assertThat(response.email()).isEqualTo("loginuser@example.com");
            assertThat(response.id()).isNotNull();
        }

        @Test
        @DisplayName("should return 401 when password is wrong")
        void login_wrongPassword() throws Exception {
            // Register a user first
            CreateUserRequest registerReq = new CreateUserRequest(
                    "Wrong Pass User",
                    "wrongpass@example.com",
                    "correctPassword"
            );

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerReq)))
                    .andExpect(status().isCreated());

            // Login with wrong password
            LoginRequest loginReq = new LoginRequest(
                    "wrongpass@example.com",
                    "wrongPassword"
            );

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginReq)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should return 401 when user does not exist")
        void login_nonExistentUser() throws Exception {
            LoginRequest loginReq = new LoginRequest(
                    "ghost@example.com",
                    "anyPassword"
            );

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginReq)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should return 400 when email is blank")
        void login_blankEmail() throws Exception {
            LoginRequest loginReq = new LoginRequest(
                    "",
                    "somePassword"
            );

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginReq)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when password is blank")
        void login_blankPassword() throws Exception {
            LoginRequest loginReq = new LoginRequest(
                    "someone@example.com",
                    ""
            );

            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginReq)))
                    .andExpect(status().isBadRequest());
        }
    }

    // -------------------------------------------------------
    //  Protected Endpoint Access Tests
    // -------------------------------------------------------
    @Nested
    @DisplayName("Protected endpoint access")
    class ProtectedEndpointTests {

        @Test
        @DisplayName("should return 401 when accessing protected endpoint without token")
        void accessProtected_noToken() throws Exception {
            mockMvc.perform(get("/api/v1/some-protected-resource")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should return 401 when accessing protected endpoint with invalid token")
        void accessProtected_invalidToken() throws Exception {
            mockMvc.perform(get("/api/v1/some-protected-resource")
                            .header("Authorization", "Bearer invalid.jwt.token")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should return 401 when Authorization header has wrong format")
        void accessProtected_malformedAuthHeader() throws Exception {
            mockMvc.perform(get("/api/v1/some-protected-resource")
                            .header("Authorization", "Basic someCredentials")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    // -------------------------------------------------------
    //  Full Auth Flow (register → login → access protected)
    // -------------------------------------------------------
    @Nested
    @DisplayName("Full authentication flow")
    class FullFlowTests {

        @Test
        @DisplayName("register → login → use JWT token — complete auth lifecycle")
        void fullAuthFlow() throws Exception {
            // Step 1: Register
            CreateUserRequest registerReq = new CreateUserRequest(
                    "E2E User",
                    "e2euser@example.com",
                    "e2ePassword123"
            );

            mockMvc.perform(post(REGISTER_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerReq)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("e2euser@example.com"));

            // Step 2: Login and get token
            LoginRequest loginReq = new LoginRequest(
                    "e2euser@example.com",
                    "e2ePassword123"
            );

            MvcResult loginResult = mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginReq)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty())
                    .andReturn();

            LoginResponse loginResponse = objectMapper.readValue(
                    loginResult.getResponse().getContentAsString(), LoginResponse.class);
            String jwtToken = loginResponse.token();

            assertThat(jwtToken).isNotBlank();

            // Step 3: Verify that the token format looks like a JWT (3 dot-separated parts)
            String[] tokenParts = jwtToken.split("\\.");
            assertThat(tokenParts).hasSize(3);

            // Step 4: Verify unauthenticated access is still blocked
            mockMvc.perform(get("/api/v1/some-protected-resource")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }
}
