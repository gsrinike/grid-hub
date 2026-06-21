package eu.gridhub.auth.model;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(@NotBlank String name, String description) {
}
