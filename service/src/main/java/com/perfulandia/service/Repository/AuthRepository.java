package com.perfulandia.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.service.model.AuthUser;

@Repository
// @Query("SELECT a FROM Auth a WHERE a.username = :username OR a.email =
// :email")
public interface AuthRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername(String username);
    /**
     * @param username
     * @param email
     * @return
     */

    @EntityGraph(attributePaths = { "roles" }, type = EntityGraphType.FETCH)
    Optional<AuthUser> findByUsernameOrEmail(String username, String email);

    AuthUser save(AuthUser authUser);

    public interface AuthService {
        AuthUser findByUsername(String username);

        String authenticate(String username, String password);

        boolean logout(String username);

        boolean register(String username, String password);

        boolean updateProfile(String username, String email);

        boolean deleteAccount(String username);

        boolean resetPassword(String username, String newPassword);

        boolean changePassword(String username, String oldPassword, String newPassword);

        boolean sendPasswordResetEmail(String email);

        boolean verifyEmail(String email, String verificationCode);

        boolean sendVerificationEmail(String email);

        boolean verifyTwoFactorAuthentication(String username, String verificationCode);

        boolean enableTwoFactorAuthentication(String username);

        boolean disableTwoFactorAuthentication(String username);

        boolean sendTwoFactorAuthenticationCode(String username);

        boolean verifyTwoFactorAuthenticationCode(String username, String verificationCode);

        boolean enableEmailNotifications(String username);

        boolean disableEmailNotifications(String username);

        boolean enablePushNotifications(String username);

        boolean disablePushNotifications(String username);

        boolean enableSMSNotifications(String username);

        boolean disableSMSNotifications(String username);

        boolean enableInAppNotifications(String username);

        boolean disableInAppNotifications(String username);

        boolean enableDarkMode(String username);

        boolean disableDarkMode(String username);

    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();

    

    boolean existsByUsername(String username);

}
