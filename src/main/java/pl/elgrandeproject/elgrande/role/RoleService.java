package pl.elgrandeproject.elgrande.role;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.user.UserClass;
import pl.elgrandeproject.elgrande.user.UserRepository;
import pl.elgrandeproject.elgrande.user.exception.UserNotFoundException;

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

    public RoleDto saveNewRole(NewRoleDto newRoleDto) {
        Role savedRole = roleRepository.save(roleMapper.mapNewRoleDtoToEntity(newRoleDto));
        return roleMapper.mapEntityToDto(savedRole);
    }

    public void assignRoleToUser(Integer roleId, UUID userId){
        Role role = roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));

        UserClass user = userRepository.findOneById(userId)
                .orElseThrow(() -> getUserNotFoundException(userId));

        role.assignUser(user);
        user.addRole(role);
        userRepository.save(user);
    }


    private UserNotFoundException getRoleNotFoundException(Integer id) {
        return new UserNotFoundException("Role with this id " + id + " not exist");
    }

    private UserNotFoundException getUserNotFoundException(UUID id) {
        return new UserNotFoundException("User with this " + id + "  not exist");
    }
}
