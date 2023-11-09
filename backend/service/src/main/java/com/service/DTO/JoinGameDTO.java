package com.service.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinGameDTO {
    private Boolean canJoin;
    private Long gameId;
}
