package com.trixs.taskmanager.models.exceptions;

public class PasswordsDoNotEqualException extends RuntimeException {
  public PasswordsDoNotEqualException(String message) {
    super(message);
  }
}
