package com.auth0.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ProfileService profileService;

    @RequestMapping(value = "profiles", method = RequestMethod.GET)
    public List<Profile> list() {
        return profileService.list();
    }

    @RequestMapping(value ="profiles", method = RequestMethod.POST)
    public Profile create(final @Validated @RequestBody Profile profile) {
        logger.info("create invoked");
        return profileService.create(profile);
    }

    @RequestMapping(value ="profiles/{id}", method = RequestMethod.GET)
    public Profile get(final @PathVariable Long id) {
        logger.info("get invoked");
        return profileService.get(id);
    }

    @RequestMapping(value ="profiles/{id}", method = RequestMethod.PUT)
    public Profile update(final @PathVariable Long id, final @Validated @RequestBody Profile profile) {
        logger.info("update invoked");
        return profileService.update(id, profile);
    }

    @RequestMapping(value ="profiles/{id}", method = RequestMethod.DELETE)
    public Profile delete(final @PathVariable Long id) {
        logger.info("delete invoked");
        return profileService.delete(id);
    }

}
