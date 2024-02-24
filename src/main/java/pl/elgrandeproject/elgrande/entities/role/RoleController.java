package pl.elgrandeproject.elgrande.entities.role;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.role.dto.NewRoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.RoleDto;
import pl.elgrandeproject.elgrande.entities.role.dto.UpdateRoleDto;

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

    @GetMapping("/id/{roleId}")
    public RoleDto getRole(@PathVariable UUID roleId){
        return roleService.getRoleById(roleId);
    }

    @GetMapping("/{name}")
    public RoleDto getRoleByName(@PathVariable String name){
        return roleService.getRoleByName(name);
    }

    @PostMapping
    public RoleDto createNewRole(@Valid @RequestBody NewRoleDto newRoleDto) {
        return roleService.saveNewRole(newRoleDto);
    }

    @PutMapping("/{roleId}/users/{userId}")
    public void assignRoleToUser(@PathVariable UUID roleId, @PathVariable UUID userId) {
        roleService.assignRoleToUser(roleId, userId);
    }

    @PatchMapping("{roleId}/users/{userId}")
    public void changeRoleToUser(@PathVariable UUID roleId, @PathVariable UUID userId,@Valid @RequestBody UpdateRoleDto updatedRoleDto) {
        roleService.changeRoleToUser(roleId, userId, updatedRoleDto);
    }

    @DeleteMapping("/{roleId}")
    public void deleteRoleById(@PathVariable UUID roleId){
        roleService.deleteRoleById(roleId);
    }

    @PatchMapping("/{roleId}")
    public void updateRoleById(@PathVariable UUID roleId, @Valid @RequestBody NewRoleDto updateRoleDto){
        roleService.updateRoleById(roleId, updateRoleDto);
    }
}
