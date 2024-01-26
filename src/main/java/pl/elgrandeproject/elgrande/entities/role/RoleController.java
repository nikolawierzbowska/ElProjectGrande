package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin/roles")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/id/{id}")
    public RoleDto getRole(@PathVariable UUID id){
        return roleService.getRoleById(id);
    }

    @GetMapping("/{name}")
    public RoleDto getRoleByName(@PathVariable String name){
        return roleService.getRoleByName(name);
    }

    @PostMapping
    public RoleDto createNewRole(@RequestBody NewRoleDto newRoleDto) {
        return roleService.saveNewRole(newRoleDto);
    }

    @PutMapping("/{roleId}/users/{userId}")
    public void assignRoleToUser(@PathVariable UUID roleId, @PathVariable UUID userId) {
        roleService.assignRoleToUser(roleId, userId);
    }

    @PatchMapping("/{roleId}/users/{userId}")
    public void changeRoleToUser(@PathVariable UUID roleId, @PathVariable UUID userId,@RequestBody NewRoleDto updatedRoleDto) {
        roleService.changeRoleToUser(roleId, userId, updatedRoleDto);
    }
}
