package com.phananh.e_commerce.authentication.application.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phananh.e_commerce.authentication.presentation.dto.request.AuthenticationRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.IntrospectRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.LogoutRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RefreshTokenRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RegisterRequest;
import com.phananh.e_commerce.authentication.application.dto.response.AuthTokenResponse;
import com.phananh.e_commerce.authentication.application.dto.response.IntrospectResponse;
import com.phananh.e_commerce.authentication.application.dto.response.LogoutResponse;
import com.phananh.e_commerce.core.util.PasswordUtils;
import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.UserCredentials;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataRoleRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import com.phananh.e_commerce.authentication.application.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String TOKEN_TYPE_BEARER = "Bearer";
	private static final String ACCESS_TYPE = "access";
	private static final String REFRESH_TYPE = "refresh";

	private final SpringDataUserRepository springDataUserRepository;
	private final SpringDataRoleRepository springDataRoleRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final byte[] jwtSecret;
	private final long accessTokenExpirationSeconds;
	private final long refreshTokenExpirationSeconds;

	private final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

	public AuthenticationServiceImpl(
			SpringDataUserRepository springDataUserRepository,
			SpringDataRoleRepository springDataRoleRepository,
			@Value("${application.security.jwt.secret-key}") String jwtSecret,
			@Value("${application.security.jwt.expiration}") long accessTokenExpirationSeconds,
			@Value("${application.security.jwt.refresh-expiration}") long refreshTokenExpirationSeconds
	) {
		this.springDataUserRepository = springDataUserRepository;
		this.springDataRoleRepository = springDataRoleRepository;
		this.jwtSecret = jwtSecret.getBytes(StandardCharsets.UTF_8);
		this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
		this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
	}

	@Override
	public AuthTokenResponse login(AuthenticationRequest request) {
		User user = springDataUserRepository.findByCredentials_Username(request.getUsername())
				.orElseThrow(() -> unauthorized("Invalid username or password"));

		if (user.getCredentials() == null || !Boolean.TRUE.equals(user.getCredentials().isEnabled())) {
			throw unauthorized("Account is disabled");
		}

		if (!PasswordUtils.matches(request.getPassword(), user.getCredentials().password())) {
			throw unauthorized("Invalid username or password");
		}

		return issueTokenPair(user);
	}

	@Override
	public AuthTokenResponse register(RegisterRequest request) {
		if (springDataUserRepository.existsByCredentialsUsername(request.getUsername())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
		}

		if (request.getEmail() != null && !request.getEmail().isBlank() && springDataUserRepository.existsByInfoEmail(request.getEmail())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}

		Role customerRole = springDataRoleRepository.findByName(RoleName.ROLE_CUSTOMER)
				.orElseGet(() -> springDataRoleRepository.save(Role.builder().name(RoleName.ROLE_CUSTOMER).build()));

		User user = new User();
		setField(user, "credentials", UserCredentials.of(request.getUsername(), request.getPassword(), true));
		setField(user, "info", null);
		setField(user, "roles", new HashSet<>(Set.of(customerRole)));

		User savedUser = springDataUserRepository.save(user);
		return issueTokenPair(savedUser);
	}

	@Override
	public AuthTokenResponse refreshToken(RefreshTokenRequest request) {
		TokenClaims claims = parseAndValidateToken(request.getRefreshToken(), REFRESH_TYPE);
		invalidatedTokens.add(request.getRefreshToken());

		User user = springDataUserRepository.findByCredentials_Username(claims.username())
				.orElseThrow(() -> unauthorized("User not found"));

		if (user.getCredentials() == null || !Boolean.TRUE.equals(user.getCredentials().isEnabled())) {
			throw unauthorized("Account is disabled");
		}

		return issueTokenPair(user);
	}

	@Override
	public LogoutResponse logout(LogoutRequest request) {
		invalidatedTokens.add(request.getToken());
		return LogoutResponse.builder().success(true).build();
	}

	@Override
	public IntrospectResponse introspect(IntrospectRequest request) {
		try {
			TokenClaims claims = parseAndValidateToken(request.getToken(), null);
			return IntrospectResponse.builder()
					.active(true)
					.username(claims.username())
					.tokenType(claims.tokenType())
					.expiresAt(claims.expiresAt())
					.build();
		} catch (ResponseStatusException ex) {
			return IntrospectResponse.builder().active(false).build();
		}
	}

//	@Override
//	public IntrospectResponse introspect(IntrospectRequest request) {
//		if (invalidatedTokens.contains(request.getToken())) {
//			return IntrospectResponse.builder().active(false).build();
//		}
//
//		try {
//			TokenClaims claims = parseAndValidateToken(request.getToken(), null, true);
//			return IntrospectResponse.builder()
//					.active(true)
//					.username(claims.username())
//					.tokenType(claims.tokenType())
//					.expiresAt(claims.expiresAt())
//					.build();
//		} catch (ResponseStatusException ex) {
//			return IntrospectResponse.builder().active(false).build();
//		}
//	}

	private AuthTokenResponse issueTokenPair(User user) {
		String username = user.getCredentials().username();
		String roles = user.getRoles().stream()
				.map(this::extractRoleName)
				.reduce((left, right) -> left + " " + right)
				.orElse("");

		String accessToken = generateToken(username, roles, ACCESS_TYPE, accessTokenExpirationSeconds);
		String refreshToken = generateToken(username, roles, REFRESH_TYPE, refreshTokenExpirationSeconds);

		return AuthTokenResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType(TOKEN_TYPE_BEARER)
				.expiresIn(accessTokenExpirationSeconds)
				.refreshExpiresIn(refreshTokenExpirationSeconds)
				.build();
	}

	private String generateToken(String username, String roles, String tokenType, long ttlSeconds) {
		long now = Instant.now().getEpochSecond();
		long exp = now + ttlSeconds;

		Map<String, Object> header = Map.of(
				"alg", "HS256",
				"typ", "JWT"
		);

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("sub", username);
		payload.put("roles", roles);
		payload.put("type", tokenType);
		payload.put("iat", now);
		payload.put("exp", exp);
		payload.put("jti", UUID.randomUUID().toString());

		try {
			String encodedHeader = encodeBase64Url(objectMapper.writeValueAsBytes(header));
			String encodedPayload = encodeBase64Url(objectMapper.writeValueAsBytes(payload));
			String signingInput = encodedHeader + "." + encodedPayload;
			String signature = encodeBase64Url(sign(signingInput));
			return signingInput + "." + signature;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot generate token", ex);
		}
	}

	private TokenClaims parseAndValidateToken(String token, String expectedType) {
		if (token == null || token.isBlank()) {
			throw unauthorized("Token is required");
		}

		if (invalidatedTokens.contains(token)) {
			throw unauthorized("Token has been invalidated");
		}

		String[] parts = token.split("\\.");
		if (parts.length != 3) {
			throw unauthorized("Invalid token format");
		}

		String signingInput = parts[0] + "." + parts[1];
		byte[] actualSignature = decodeBase64Url(parts[2]);
		byte[] expectedSignature = sign(signingInput);
		if (!MessageDigest.isEqual(actualSignature, expectedSignature)) {
			throw unauthorized("Invalid token signature");
		}

		try {
			Map<String, Object> payload = objectMapper.readValue(
					decodeBase64Url(parts[1]),
					new TypeReference<>() {
					}
			);

			String username = Optional.ofNullable(payload.get("sub")).map(Object::toString).orElse("");
			String tokenType = Optional.ofNullable(payload.get("type")).map(Object::toString).orElse("");
			long exp = asLong(payload.get("exp"));

			if (expectedType != null && !expectedType.equals(tokenType)) {
				throw unauthorized("Invalid token type");
			}

			if (Instant.now().getEpochSecond() >= exp) {
				throw unauthorized("Token has expired");
			}

			if (username.isBlank()) {
				throw unauthorized("Token subject is missing");
			}

			return new TokenClaims(username, tokenType, exp);
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw unauthorized("Invalid token payload");
		}
	}

	private byte[] sign(String data) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(jwtSecret, "HmacSHA256"));
			return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot sign token", ex);
		}
	}

	private String encodeBase64Url(byte[] bytes) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private byte[] decodeBase64Url(String value) {
		try {
			return Base64.getUrlDecoder().decode(value);
		} catch (IllegalArgumentException ex) {
			throw unauthorized("Invalid base64 token data");
		}
	}

	private long asLong(Object value) {
		if (value instanceof Number number) {
			return number.longValue();
		}
		if (value == null) {
			throw unauthorized("Token expiration is missing");
		}
		try {
			return Long.parseLong(value.toString());
		} catch (NumberFormatException ex) {
			throw unauthorized("Token expiration is invalid");
		}
	}

	private ResponseStatusException unauthorized(String message) {
		return new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
	}

	private void setField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		} catch (ReflectiveOperationException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot set user field", ex);
		}
	}

	private String extractRoleName(Role role) {
		try {
			Field field = role.getClass().getDeclaredField("name");
			field.setAccessible(true);
			Object value = field.get(role);
			return value == null ? "" : value.toString();
		} catch (ReflectiveOperationException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot read role name", ex);
		}
	}

	private record TokenClaims(String username, String tokenType, long expiresAt) {
	}
}



