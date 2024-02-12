package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleFoundException;
import pl.elgrandeproject.elgrande.entities.role.exception.RoleNotFoundException;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
//        return roleRepository.findByName(name)
//                .map(role -> roleMapper.mapEntityToDto(role))
//                .orElseThrow(() -> getRoleNotFoundException(name));
        if(roleRepository.findByName(name.toUpperCase())
                .map(entity -> roleMapper.mapEntityToDto(entity)).isEmpty()){
            return null;
        }
        return roleRepository.findByName(name.toUpperCase())
                .map(entity -> roleMapper.mapEntityToDto(entity)).get();
    }

    public RoleDto saveNewRole(NewRoleDto newRoleDto) {
        String upperCaseName = newRoleDto.getName().toUpperCase();
        if(roleRepository.findByName(upperCaseName).isEmpty()){
            newRoleDto.setName(upperCaseName);
            Role savedRole = roleRepository.save(roleMapper.mapNewRoleDtoToEntity(newRoleDto));
            return roleMapper.mapEntityToDto(savedRole);
        }
        throw new RoleFoundException("Taka nazwa : "+ newRoleDto.getName() + " już istnieje !");
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

        Role roleFromDb = roleRepository.findByName(updatedRoleDto.getName().toUpperCase())
                .orElseThrow(() -> getRoleNotFoundException(updatedRoleDto.getName().toUpperCase()));

        Optional<Role> oldRoleUser = user.getRoles().stream()
                .filter(currentRole -> currentRole.getId()
                        .equals(roleId)).findFirst();

        oldRoleUser.ifPresent(currentRole -> {
            if (!currentRole.getName().equals(updatedRoleDto.getName().toUpperCase())) {
                user.clearAssignRole();
                user.clearAssignRole();
                user.addRole(roleFromDb);
                (roleFromDb).assignUser(user);
                userRepository.save(user);
            }else{
                throw new RoleNotFoundException("Nie można znaleźć takiej nazwy roli");
            }
        });
    }

       public void deleteRoleById(UUID roleId) {
        Role role = roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));
        roleRepository.delete(role);
    }

    public void updateRoleById(UUID roleId, NewRoleDto updateRoleDto) {
        Role role = roleRepository.findOneById(roleId)
                .orElseThrow(() -> getRoleNotFoundException(roleId));

        String upperCaseName = updateRoleDto.getName().toUpperCase();
        if(getRoleByName(upperCaseName) == null ){
            role.setName(roleMapper.mapNewRoleDtoToEntity(updateRoleDto).getName());
            roleRepository.save(role);
        }else{
            throw new RoleFoundException("Istnieje taka nazwa: " + updateRoleDto.getName().toUpperCase());
        }
    }

    private UserNotFoundException getUserNotFoundException(UUID userId) {
        return new UserNotFoundException("Użytkownik z takim id: " + userId + "  nie istnieje");
    }

    private RoleNotFoundException getRoleNotFoundException(String name) {
        return new RoleNotFoundException("Użytkownik z taką nazwą: " + name + " nie istnieje");
    }

    private RoleNotFoundException getRoleNotFoundException(UUID roleId) {
        return new RoleNotFoundException("Rola z takim id:  " + roleId + " nie istnieje");
    }
}
