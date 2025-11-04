package com.varta.dto;

public class Auth {
    public record StartRequest(String email) {
    }

    public record StartResponse(String status, String flowId) {
    }

    public record VerifyRequest(String flowId, String code) {
    }

    public record VerifyResponse(String status, User user) {
    }

    public record User(String id, String email) {
    }
}