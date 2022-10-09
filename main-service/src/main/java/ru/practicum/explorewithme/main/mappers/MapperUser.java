package ru.practicum.explorewithme.main.mappers;

import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithme.main.user.model.UserDto;
import ru.practicum.explorewithme.main.user.model.User;

@RequiredArgsConstructor
public class MapperUser {

    public static UserDto mapUserToUserDto(User userDto) {
        return UserDto.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .build();
    }
}