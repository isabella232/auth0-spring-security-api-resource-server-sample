package com.auth0.example;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Not implemented further - but easy to do so if a real database required
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
