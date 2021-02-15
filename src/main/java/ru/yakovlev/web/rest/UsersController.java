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

package ru.yakovlev.web.rest;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yakovlev.web.resources.NewUserResource;
import ru.yakovlev.web.resources.UserAlreadyExists;

/**
 * API entry point.
 *
 * @author Yakovlev Alexander (sanyakovlev@yandex.ru)
 * @since 0.6.0
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UsersController {
    private final UserDetailsManager userManager;
    private final MessageSource messageSource;

    @PostMapping(consumes = ApiEntryPoint.CUSTOM_MIME_TYPE)
    public ResponseEntity<Object> createUser(@Valid @RequestBody final NewUserResource newUser)
            throws UserAlreadyExists {
        newUser.register(this.userManager, this.messageSource);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
