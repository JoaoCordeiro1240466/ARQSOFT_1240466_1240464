package pt.psoft.g1.psoftg1.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.api.views.UserView;
import pt.psoft.g1.psoftg1.model.Role;
import pt.psoft.g1.psoftg1.services.UserService;
import pt.psoft.g1.psoftg1.services.UserService.CreateUserRequest;
import pt.psoft.g1.psoftg1.services.UserService.EditUserRequest;


@Tag(name = "UserAdmin")
@RestController
@RequestMapping(path = "api/admin/users")
@RolesAllowed(Role.ADMIN)
@RequiredArgsConstructor
public class UserAdminApi {

	private final UserService userService;
	private final UserViewMapper userViewMapper;

	@PostMapping
	public UserView create(@RequestBody @Valid final CreateUserRequest request) {
		final var user = userService.create(request);
		return userViewMapper.toUserView(user);
	}

	@PutMapping("{id}")
	public UserView update(@PathVariable final Long id, @RequestBody @Valid final EditUserRequest request) {
		final var user = userService.update(id, request);
		return userViewMapper.toUserView(user);
	}

	@DeleteMapping("{id}")
	public UserView delete(@PathVariable final Long id) {
		final var user = userService.delete(id);
		return userViewMapper.toUserView(user);
	}

	@GetMapping("{id}")
	public UserView get(@PathVariable final Long id) {
		final var user = userService.getUser(id);
		return userViewMapper.toUserView(user);
	}
}
