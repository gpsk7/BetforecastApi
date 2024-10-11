package net.javaguides.springboot.controller;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String emailAlreadyExists) {

    }
}
