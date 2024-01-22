package pl.elgrandeproject.elgrande.role;

import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.role.dto.RoleDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping
    public RoleDto createNewRole(@RequestBody NewRoleDto newRoleDto) {
        return roleService.saveNewRole(newRoleDto);
    }

    @PutMapping("/{roleId}/users/{userId}")
    public void assignRoleToUser(@PathVariable Integer roleId, @PathVariable UUID userId) {
        roleService.assignRoleToUser(roleId, userId);
    }
}
