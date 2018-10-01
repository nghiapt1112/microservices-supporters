package com.nghiatut.mss.support.edge.web.controller;

import com.nghiatut.mss.support.edge.web.dto.Bar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@RestController
@RequestMapping("/bars")
public class BarController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public BarController() {
        super();
    }

    // API - read
    @PreAuthorize("#oauth2.hasScope('webshop') or #oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Bar findById(@PathVariable final long id) {
        LOGGER.info("Finding bar with id: {}", id);
        return new Bar(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
    }

    // API - write
    @PreAuthorize("#oauth2.hasScope('bar') and #oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Bar create(@RequestBody final Bar bar) {
        bar.setId(Long.parseLong(randomNumeric(2)));
        return bar;
    }
}
