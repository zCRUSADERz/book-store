/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Yakovlev Alexander
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.yakovlev.integration;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Map;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Authentication integration tests.
 *
 * @author Yakovlev Alexander (sanyakovlev@yandex.ru)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integration tests for authentication")
@ActiveProfiles("test")
class AuthenticationIT {

    @Test
    @DisplayName("Any HTTP request must be authenticated")
    void whenHttpGetOnRootThenResponseWithUnauthorizedError(@Autowired final WebTestClient client) {
        client.mutate().defaultCookies(Map::clear).build().get().exchange().expectStatus().isUnauthorized();
    }


    @Test
    @DisplayName("Unauthenticated HTTP request must contain in response header www-authenticate with Basic type")
    void whenHttpGetOnRootThenResponseHeadersWithWWWAuthenticateHeader(
            @Autowired final WebTestClient client) {
        client.mutate().defaultCookies(Map::clear).build().get().exchange()
                .expectHeader().valueEquals("www-authenticate", "Basic realm=\"Realm\"");
    }

    @Test
    @DisplayName("Can authenticate under the admin account")
    void whenHttpGetOnRootWithAdminAuthDataThenAuthorized(@Autowired final WebTestClient client) {
        final String headerValue = "Basic " + Base64Utils.encodeToString(("admin:admin").getBytes(UTF_8));
        client.mutate().defaultCookies(Map::clear).build().get().header("Authorization", headerValue).exchange()
                .expectStatus().value(new IsNot<>(new IsEqual<>(401)));
    }

    @Test
    @DisplayName("Can authenticate under the user account")
    void whenHttpGetOnRootWithUserAuthDataThenAuthorized(@Autowired final WebTestClient client) {
        final String headerValue = "Basic " + Base64Utils.encodeToString(("user:user").getBytes(UTF_8));
        client.mutate().defaultCookies(Map::clear).build().get().header("Authorization", headerValue).exchange()
                .expectStatus().value(new IsNot<>(new IsEqual<>(401)));
    }
}
