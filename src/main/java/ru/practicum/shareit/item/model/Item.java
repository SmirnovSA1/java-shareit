package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Item {
    private Long id;
    @NotEmpty
    @NotBlank(message = "name обязателен к заполнению")
    private String name;
    @NotEmpty
    @NotBlank(message = "description обязателен к заполнению")
    private String description;
    @NotEmpty
    @NotNull
    @NotBlank(message = "available обязателен к заполнению")
    private Boolean available;
    private Long owner;
    private Long request;
}
