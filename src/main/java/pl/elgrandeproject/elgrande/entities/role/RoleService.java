package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAllBy().stream().
                map(role ->roleMapper.mapEntityToDto(role))
                .toList();
    }

    public RoleDto getRole(UUID id) {
        return roleRepository.findOneById(id)
                .map(role -> roleMapper.mapEntityToDto(role))
                .orElseThrow(() -> getRoleNotFoundException(id));
    }

    public RoleDto saveNewRole(NewRoleDto newRoleDto) {
        Role savedRole = roleRepository.save(roleMapper.mapNewRoleDtoToEntity(newRoleDto));
        return roleMapper.mapEntityToDto(savedRole);
    }

    public void assignRoleToUser(UUID roleId, UUID userId){
        Role role = roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));

        UserClass user = userRepository.findOneById(userId)
                .orElseThrow(() -> getUserNotFoundException(userId));

        role.assignUser(user);
        user.addRole(role);
        userRepository.save(user);
    }


    private UserNotFoundException getRoleNotFoundException(UUID id) {
        return new UserNotFoundException("Role with this id " + id + " not exist");
    }

    private UserNotFoundException getUserNotFoundException(UUID id) {
        return new UserNotFoundException("User with this " + id + "  not exist");
    }
}
