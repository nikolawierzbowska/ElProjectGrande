package pl.elgrandeproject.elgrande.role;

import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.role.dto.RoleDto;

@Component
public class RoleMapper {

    public Role mapNewRoleDtoToEntity(NewRoleDto newRoleDto) {
        return new Role(newRoleDto.id(), newRoleDto.name());
    }

    public RoleDto mapEntityToDto(Role role){
        return new RoleDto(role.getId(), role.getName());
    }


}
