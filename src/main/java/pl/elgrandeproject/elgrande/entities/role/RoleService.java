package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleFoundException;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.elgrandeproject.elgrande.entities.user.UserService.getUserNotFoundException;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

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
        return roleRepository.findByName(name.toUpperCase())
                .map(role -> roleMapper.mapEntityToDto(role))
                .orElseThrow(() -> getRoleWithThisNameNotFoundException(name));
    }

    public boolean ifPresentRoleWithThisName(NewRoleDto newRoleDto) {
        return roleRepository.findByName(newRoleDto.getName().toUpperCase())
                .isPresent();
    }

    public RoleDto saveNewRole(NewRoleDto newRoleDto) {
        String upperCaseName = newRoleDto.getName().toUpperCase();
        if (!ifPresentRoleWithThisName(newRoleDto)) {
            newRoleDto.setName(upperCaseName);
            Role savedRole = roleRepository.save(roleMapper.mapNewRoleDtoToEntity(newRoleDto));
            return roleMapper.mapEntityToDto(savedRole);
        }
        throw new RoleFoundException("Taka nazwa : " + newRoleDto.getName() + " już istnieje !");
    }

    public Role findRoleById(UUID roleId) {
        return roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));
    }


    public Role findRoleByName(NewRoleDto newRoleDto) {
        return roleRepository.findByName(newRoleDto.getName().toUpperCase())
                .orElseThrow(() -> getRoleWithThisNameNotFoundException(newRoleDto.getName()));
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

    public void changeRoleToUser(UUID oldRoleId, UUID userId, NewRoleDto updatedRoleDto) {
        findRoleByName(updatedRoleDto);
        UserClass user = findUserById(userId);
        Optional<Role> oldRoleUserToChange = user.getRoles().stream()
                .filter(currentRole -> currentRole.getId()
                        .equals(oldRoleId)).findFirst();

        if (ifPresentRoleWithThisName((updatedRoleDto)) && oldRoleUserToChange.isPresent()) {
            user.clearAssignRole();
            user.addRole(findRoleByName(updatedRoleDto));
            findRoleByName(updatedRoleDto).assignUser(user);
            userRepository.save(user);
        } else {

            throw new RuntimeException("nie zgdza się");
        }
    }

    public void deleteRoleById(UUID roleId) {
        Role role = roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));
        roleRepository.delete(role);
    }

    public void updateRoleById(UUID roleId, NewRoleDto updateRoleDto) {
        Role role = roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));
        updateRoleDto.setName(updateRoleDto.getName().toUpperCase());
        if (!ifPresentRoleWithThisName(updateRoleDto)) {
            role.setName(roleMapper.mapNewRoleDtoToEntity(updateRoleDto).getName());
            roleRepository.save(role);
        } else {
            throw new RoleFoundException("Istnieje taka nazwa: " + updateRoleDto.getName().toUpperCase());
        }
    }

    private RoleNotFoundException getRoleWithThisNameNotFoundException(String name) {
        return new RoleNotFoundException("Rola z taką nazwą: " + name + " nie istnieje");
    }

    private RoleNotFoundException getRoleNotFoundException(UUID roleId) {
        return new RoleNotFoundException("Rola z takim id:  " + roleId + " nie istnieje");
    }
}
