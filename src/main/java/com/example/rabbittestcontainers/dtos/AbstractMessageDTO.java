package com.example.rabbittestcontainers.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public abstract class AbstractMessageDTO implements Serializable {

    protected String uuid;
    protected String timestamp;
}
