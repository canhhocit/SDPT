package com.phananh.e_commerce.authentication.application.service;

import com.phananh.e_commerce.authentication.presentation.dto.request.AuthenticationRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.IntrospectRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.LogoutRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RefreshTokenRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RegisterRequest;
import com.phananh.e_commerce.authentication.application.dto.response.AuthTokenResponse;
import com.phananh.e_commerce.authentication.application.dto.response.IntrospectResponse;
import com.phananh.e_commerce.authentication.application.dto.response.LogoutResponse;

public interface AuthenticationService {
	AuthTokenResponse login(AuthenticationRequest request);

	AuthTokenResponse register(RegisterRequest request);

	AuthTokenResponse refreshToken(RefreshTokenRequest request);

	LogoutResponse logout(LogoutRequest request);

	IntrospectResponse introspect(IntrospectRequest request);
}


