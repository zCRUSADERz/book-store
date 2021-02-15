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

import static org.hamcrest.MatcherAssert.assertThat;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * New user resource integration test.
 *
 * @author Yakovlev Alexander (sanyakovlev@yandex.ru)
 * @since 0.6.0
 */
class NewUserResourceIT {
    private static final String USERNAME_PROPERTY = "username";
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("Username must not be empty string")
    void whenUsernameIsEmptyThenResourceIsNotValid() {
        final var newUserResource = new NewUserResource("", "password");
        this.validate(newUserResource, USERNAME_PROPERTY);
    }

    @Test
    @DisplayName("Username must not be null")
    void whenUsernameIsNullThenResourceIsNotValid() {
        final var newUserResource = new NewUserResource(null, "password2");
        this.validate(newUserResource, USERNAME_PROPERTY);
    }

    @Test
    @DisplayName("Username must not be blank")
    void whenUsernameIsBlankThenResourceIsNotValid() {
        final var newUserResource = new NewUserResource(" ", "password3");
        this.validate(newUserResource, USERNAME_PROPERTY);
    }

    @Test
    @DisplayName("Password length must be at least 6 characters")
    void whenPasswordLengthIsFiveThenResourceIsNotValid() {
        final var newUserResource = new NewUserResource("admin", "12345");
        this.validate(newUserResource, "password");
    }

    private void validate(final NewUserResource newUserResource, final String property) {
        final var violations = validator.validateProperty(newUserResource, property);
        assertThat(violations, new IsNot<>(new IsEmptyCollection<>()));
    }
}
