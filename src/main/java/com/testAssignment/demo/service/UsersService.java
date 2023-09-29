package com.testAssignment.demo.service;

import com.testAssignment.demo.dto.RequestUpdateUserDTO;
import com.testAssignment.demo.model.Users;
import com.testAssignment.demo.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Value("${user.minAge}")
    private int minAge;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users createUser(RequestUpdateUserDTO users) {

        try {

            Users createdUser = new Users();

            createdUser.setFirstName(users.getFirstName());
            createdUser.setLastName(users.getLastName());
            createdUser.setEmail(users.getEmail());
            createdUser.setBirthDate(users.getBirthDate());
            createdUser.setAddress(users.getAddress());
            createdUser.setPhoneNumber(users.getPhoneNumber());

            LocalDate currentDate = LocalDate.now();
            LocalDate dateOfBirth = users.getBirthDate();
            int getFinalAge = currentDate.getYear() - dateOfBirth.getYear();

            if (getFinalAge < minAge) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("The user must be older than %d", minAge));
            }
            return createdUser;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during user creation");
        }
    }

    public Users patchUpdateUserById(Long id, RequestUpdateUserDTO requestUpdateUserDTO) {

        try {

            Users updatedUser = existedUser(id);

            updatedUser.setFirstName(requestUpdateUserDTO.getFirstName());
            updatedUser.setLastName(requestUpdateUserDTO.getLastName());
            updatedUser.setEmail(requestUpdateUserDTO.getEmail());
            updatedUser.setBirthDate(requestUpdateUserDTO.getBirthDate());
            updatedUser.setAddress(requestUpdateUserDTO.getAddress());
            updatedUser.setPhoneNumber(requestUpdateUserDTO.getPhoneNumber());

            return usersRepository.save(updatedUser);
        }
        catch (ResponseStatusException ex) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Users putUpdateUserById(Long id, RequestUpdateUserDTO requestUpdateUserDTO) {

        try {

            Users users = existedUser(id);

            users.setFirstName(requestUpdateUserDTO.getFirstName());
            users.setLastName(requestUpdateUserDTO.getLastName());
            users.setEmail(requestUpdateUserDTO.getEmail());
            users.setBirthDate(requestUpdateUserDTO.getBirthDate());
            users.setAddress(requestUpdateUserDTO.getAddress());
            users.setPhoneNumber(requestUpdateUserDTO.getPhoneNumber());

            return usersRepository.save(users);
        }
        catch (ResponseStatusException ex) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Long deleteUserById(Long id) {

        try {

            usersRepository.deleteById(id);
            return id;
        }
        catch (ResponseStatusException e) {

            System.out.println("Status: " + HttpStatus.NO_CONTENT + " message: " + e.getMessage());
            return -1L;
        }
        catch (EmptyResultDataAccessException e) {

            System.out.println("User with Id " + id + " not found");
            return -2L;
        }
        catch (Exception e) {

            System.out.println("An error occurred while deleting employee: " + e.getMessage());
            return -3L;
        }
    }

    public List<Users> findUsersWithDateRange(LocalDate fromDate, LocalDate toDate) {

        try {

            return usersRepository.findUsersByBirthDate(fromDate, toDate);
        }
        catch (DateTimeException exception) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private Users existedUser(Long id) {

        return usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with [%d] not found", id)
                ));
    }
}