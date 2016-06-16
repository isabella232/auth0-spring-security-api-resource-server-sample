package com.auth0.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Possible scope values for this sample:
 *
 * "scope" -> "read:profiles read:profile write:profile delete:profile"
 */
@Service
public class ProfileService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    protected ProfileRepository profileRepository;
    protected ProfileRepositoryStub profileRepository;

    private Auth0Client auth0Client;

    @Autowired
    public ProfileService(final Auth0Client auth0Client, final ProfileRepositoryStub profileRepository) {
        this.auth0Client = auth0Client;
        this.profileRepository = profileRepository;
    }

    @PreAuthorize("hasAuthority('read:profiles')")
    public List<Profile> list() {
        return profileRepository.list();
    }

    @PreAuthorize("hasAuthority('write:profile')")
    public Profile create(Profile profile) {
        return profileRepository.create(profile);
    }

    // demonstration only of having more than one authority declaration
    @PreAuthorize("hasAuthority('read:profile') or hasAuthority('read:profiles')")
    public Profile get(Long id) {
        return profileRepository.get(id);
    }

    @PreAuthorize("hasAuthority('write:profile')")
    public Profile update(Long id, Profile profile) {
        return profileRepository.update(id, profile);
    }

    @PreAuthorize("hasAuthority('delete:profile')")
    public Profile delete(Long id) {
        return profileRepository.delete(id);
    }

}


