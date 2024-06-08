package com.streak.dsastreak.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest
{
    private String username;
    private String password;
}
