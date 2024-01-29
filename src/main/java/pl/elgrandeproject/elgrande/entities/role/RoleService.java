package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
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
                map(role -> roleMapper.mapEntityToDto(role))
                .toList();
    }

    public RoleDto getRoleById(UUID roleId) {
        return roleRepository.findOneById(roleId)
                .map(role -> roleMapper.mapEntityToDto(role))
                .orElseThrow(() -> getRoleNotFoundException(roleId));
    }

    public RoleDto getRoleByName(String name) {
        return roleRepository.findByName(name)
                .map(role -> roleMapper.mapEntityToDto(role))
                .orElseThrow(() -> getRoleNotFoundException(name));
    }

    public RoleDto saveNewRole(NewRoleDto newRoleDto) {
        Role savedRole = roleRepository.save(roleMapper.mapNewRoleDtoToEntity(newRoleDto));
        return roleMapper.mapEntityToDto(savedRole);
    }

    public Role findRoleById(UUID roleId) {
        return roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));
    }

    public UserClass findUserById(UUID userId) {
        return userRepository.findOneById(userId)
                .orElseThrow(() -> getUserNotFoundException(userId));
    }

    public void assignRoleToUser(UUID roleId, UUID userId) {
        Role role = findRoleById(roleId);
        UserClass user = findUserById(userId);

        role.assignUser(user);
        user.addRole(role);
        userRepository.save(user);
    }

    public void changeRoleToUser(UUID roleId, UUID userId, NewRoleDto updatedRoleDto) {
        UserClass user = findUserById(userId);

        Role roleFromDb = roleRepository.findByName(updatedRoleDto.name())
                .orElseThrow(() -> getRoleNotFoundException(updatedRoleDto.name()));

        Optional<Role> oldRoleUser = user.getRoles().stream()
                .filter(currentRole -> currentRole.getId()
                        .equals(roleId)).findFirst();

        oldRoleUser.ifPresent(currentRole -> {
            if (!currentRole.getName().equals(updatedRoleDto.name())) {
                user.clearAssignRole();
                user.clearAssignRole();
                user.addRole(roleFromDb);
                (roleFromDb).assignUser(user);
            }
        });
        userRepository.save(user);
    }

    private RoleNotFoundException getRoleNotFoundException(UUID roleId) {
        return new RoleNotFoundException("Role with this id = " + roleId + " not exist");
    }

    private UserNotFoundException getUserNotFoundException(UUID userId) {
        return new UserNotFoundException("User with this = " + userId + "  not exist");
    }
    private RoleNotFoundException getRoleNotFoundException(String name) {
        return new RoleNotFoundException("Role with this name " + name + " not exist");
    }
}
