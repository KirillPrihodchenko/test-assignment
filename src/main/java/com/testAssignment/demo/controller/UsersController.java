package com.testAssignment.demo.controller;

import com.testAssignment.demo.dto.RequestUpdateUserDTO;
import com.testAssignment.demo.model.Users;
import com.testAssignment.demo.service.UsersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@AllArgsConstructor

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/")
    public ResponseEntity<?> getAllUsersWithDateRange(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {

        return new ResponseEntity<>(
                usersService.findUsersWithDateRange(fromDate, toDate),
                HttpStatus.OK
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(
            @RequestBody @Valid RequestUpdateUserDTO requestUpdateUserDTO) {

        return new ResponseEntity<>(
                usersService.createUser(requestUpdateUserDTO),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}/partUpdate")
    public ResponseEntity<?> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody @Valid RequestUpdateUserDTO requestUpdateUserDTO) {

        return new ResponseEntity<>(
                usersService.patchUpdateUserById(id, requestUpdateUserDTO),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/totalUpdate")
    public ResponseEntity<Users> totalUpdateUser(
            @PathVariable Long id,
            @RequestBody @Valid RequestUpdateUserDTO requestUpdateUserDTO) {

        return new ResponseEntity<>(
                usersService.putUpdateUserById(id, requestUpdateUserDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}/delete")
    public Long deleteUserById(@PathVariable Long id) {

        return usersService.deleteUserById(id);
    }
}