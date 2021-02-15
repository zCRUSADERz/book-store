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

package ru.yakovlev.web.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * New user resource unit test.
 *
 * @author Yakovlev Alexander (sanyakovlev@yandex.ru)
 * @since 0.6.0
 */
@ExtendWith(MockitoExtension.class)
class NewUserResourceTest {
    @Mock
    private UserDetailsManager userDetailsManager;
    @Mock
    private MessageSource messageSource;

    @Test
    @DisplayName("When registering a new user, the #createUser method must be called in UserDetailsManager.")
    void whenRegisterThenCreateUserMethodInvokedOnUserDetailsManager() throws UserAlreadyExists {
        final var username = "user";
        final var password = "password";
        final var newUser = new NewUserResource(username, password);
        newUser.register(this.userDetailsManager, this.messageSource);
        final ArgumentMatcher<UserDetails> argMatcher =
                userDetails -> userDetails.getUsername().equals(username) && userDetails.getPassword().equals(password);
        verify(this.userDetailsManager).createUser(Mockito.argThat(argMatcher));
    }

    @Test
    @DisplayName("An UserAlreadyExists should be thrown when registering an existing user")
    void whenRegisterAlreadyExistsUserThenThrowUserAlreadyExistsException() {
        final var username = "admin";
        final var password = "admin";
        final var newUser = new NewUserResource(username, password);
        doThrow(DuplicateKeyException.class).when(this.userDetailsManager).createUser(any());
        final Executable executable = () -> newUser.register(this.userDetailsManager, this.messageSource);
        Assertions.assertThrows(UserAlreadyExists.class, executable);
    }
}