package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;

import java.util.List;

@Component
public class RoleMapper {

    public Role mapNewRoleDtoToEntity(NewRoleDto newRoleDto) {
        return new Role(newRoleDto.getName());
    }

    public RoleDto mapEntityToDto(Role role){
        return new RoleDto(role.getId(), role.getName(), mapUsers(role));
    }

    private List<RoleDto.UserNameId> mapUsers(Role entity){        {
        return  entity.getUsers().stream()
                .map(u-> new RoleDto.UserNameId(u.getId(), u.getEmail()))
                .toList();
        }
    }
}
