package com.nghiatut.mss.support.edge.web.controller;

import com.nghiatut.mss.support.edge.web.dto.Foo;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Controller
@RequestMapping("/foos")
public class FooController {

    public FooController() {
        super();
    }

    // API - read
    @PreAuthorize("isMember(#id)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public Foo findById(@PathVariable final long id) {
        return new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
    }

    // API - write
    @PreAuthorize("hasAuthority('ROLE_READ')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Foo create(@RequestBody final Foo foo) {
        foo.setId(Long.parseLong(randomNumeric(2)));
        return foo;
    }

}
