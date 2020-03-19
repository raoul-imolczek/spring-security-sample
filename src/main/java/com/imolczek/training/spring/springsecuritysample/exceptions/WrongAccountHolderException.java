package com.imolczek.training.spring.springsecuritysample.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class WrongAccountHolderException extends RuntimeException {

}
