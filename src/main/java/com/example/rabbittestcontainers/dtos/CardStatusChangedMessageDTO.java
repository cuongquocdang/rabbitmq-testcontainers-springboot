package com.example.rabbittestcontainers.dtos;

import com.example.rabbittestcontainers.enums.CardStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CardStatusChangedMessageDTO extends AbstractMessageDTO implements Serializable {

    private CardStatus cardStatus;
}
