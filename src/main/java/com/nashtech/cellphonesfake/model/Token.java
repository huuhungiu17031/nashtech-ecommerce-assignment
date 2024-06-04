package com.nashtech.cellphonesfake.model;

import com.nashtech.cellphonesfake.enumeration.TokenType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private Boolean expired = false;
    private Boolean revoked = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
