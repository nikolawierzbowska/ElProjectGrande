package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;

@Component
public class RoleMapper {

    public Role mapNewRoleDtoToEntity(NewRoleDto newRoleDto) {
        return new Role(newRoleDto.name());
    }

    public RoleDto mapEntityToDto(Role role){
        return new RoleDto(role.getId(), role.getName());
    }


}
