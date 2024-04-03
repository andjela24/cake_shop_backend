package com.andjela.diplomski.entity;

import com.andjela.diplomski.common.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "tokens_password_reset")
public class PasswordResetToken extends BaseEntity {
    public static final int EXPIRATION_MINUTES = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @EqualsAndHashCode.Exclude
    private ZonedDateTime expiryDate;

    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "char(36)")
    private UUID uuid;

    private ZonedDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        return ZonedDateTime.now().plusMinutes(expiryTimeInMinutes);
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION_MINUTES);
    }

    public boolean isExpired() {
        Duration duration = Duration.between(ZonedDateTime.now(), expiryDate);
        return duration.getSeconds() <= 0;
    }

}
