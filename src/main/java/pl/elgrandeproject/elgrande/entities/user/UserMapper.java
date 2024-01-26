package pl.elgrandeproject.elgrande.entities.user;

import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.entities.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;

import java.util.List;

@Component
public class UserMapper {

    public UserClass mapNewDtoToEntity(NewUserDto dto) {
        return new UserClass(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRepeatedPassword()
        );
    }

    public UserDto mapEntityToDto(UserClass entity) {
        return new UserDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRepeatedPassword(),
                mapRoleToDto(entity)

        );
    }

    public List<UserDto.UserRole> mapRoleToDto(UserClass user){
        return  user.getRoles().stream()
                .map(role -> new UserDto.UserRole(role.getId(), role.getName()))
                .toList();
    }
}
