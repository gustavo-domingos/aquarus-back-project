package com.gustavoalves.complementary_activities.infra.user;

import com.gustavoalves.complementary_activities.core.config.services.TokenService;
import com.gustavoalves.complementary_activities.domain.user.Login;
import com.gustavoalves.complementary_activities.domain.user.User;
import com.gustavoalves.complementary_activities.domain.user.UserUseCase;
import com.gustavoalves.complementary_activities.utils.entities.Pagination;
import com.gustavoalves.complementary_activities.utils.entities.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<Response<User>> createUser(@RequestBody User user) {
        final var response = userUseCase.create(user);

        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Response<User>> updateUser(@RequestBody User user) {
        final var response = userUseCase.update(user);

        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Response<Pagination<User>>> pagination(
            @RequestParam(name = "page", defaultValue = "0")
            int page,
            @RequestParam(name = "count", defaultValue = "10")
            int count
    ) {
        final var response = userUseCase.getPagination(page, count);

        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Response<User>> getByUUID(
            @PathVariable(name = "uuid")
            UUID uuid
    ) {
        final var response = userUseCase.getByUUID(uuid);

        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody Login login) {
        final var response = userUseCase.login(login);

        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().header(
                HttpHeaders.SET_COOKIE,
                tokenService.generateTokenCookie(response.getEntity()).toString()
        ).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout() {
        return ResponseEntity.ok().header(
                HttpHeaders.SET_COOKIE,
                tokenService.getCleanCookie().toString()
        ).body(true);
    }
}
