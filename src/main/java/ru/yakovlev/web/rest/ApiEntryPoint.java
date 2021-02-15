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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yakovlev.web.resources.ApiRelations;

/**
 * API entry point.
 *
 * @author Yakovlev Alexander (sanyakovlev@yandex.ru)
 * @since 0.6.0
 */
@RestController
@RequestMapping("/api")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEntryPoint {
    public static final String CUSTOM_MIME_TYPE = "application/vnd.ru.yakovlev.book-store.v1+json";

    /**
     * List of all API relations.
     *
     * @return List of all API relations.
     */
    @GetMapping
    public EntityModel<ApiRelations> relations() {
        final var model = EntityModel.of(ApiRelations.of());
        model.add(linkTo(ApiEntryPoint.class).withSelfRel());
        return model;
    }
}
