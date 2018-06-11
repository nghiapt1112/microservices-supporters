package com.nghiatut.mss.support.edge.web.controller;

import com.nghiatut.mss.support.edge.web.dto.Bar;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Controller
@RequestMapping("/bars")
public class BarController {

    public BarController() {
        super();
    }

    // API - read
    @PreAuthorize("#oauth2.hasScope('bar') and #oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public Bar findById(@PathVariable final long id) {
        return new Bar(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
    }

    // API - write
    @PreAuthorize("#oauth2.hasScope('bar') and #oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Bar create(@RequestBody final Bar bar) {
        bar.setId(Long.parseLong(randomNumeric(2)));
        return bar;
    }
}
