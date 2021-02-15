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

import java.util.Collections;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * Resource of new user.
 *
 * @author Yakovlev Alexander (sanyakovlev@yandex.ru)
 * @since 0.6.0
 */
@AllArgsConstructor
public class NewUserResource {
    @NotBlank
    private final String username;
    @Size(min = 6)
    private final String password;

    /**
     * Create new user.
     *
     * @param userDetailsManager user details manager.
     * @throws UserAlreadyExists if username already exists.
     */
    public void register(final UserDetailsManager userDetailsManager, final MessageSource messageSource)
            throws UserAlreadyExists {
        try {
            final var grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority("login"));
            userDetailsManager.createUser(new User(this.username, this.password, grantedAuthorities));
        } catch (DuplicateKeyException e) {
            final var args = new Object[] { this.username };
            final var locale = LocaleContextHolder.getLocale();
            throw new UserAlreadyExists(messageSource.getMessage("users.user.already.exists", args, locale), e);
        }
    }
}
