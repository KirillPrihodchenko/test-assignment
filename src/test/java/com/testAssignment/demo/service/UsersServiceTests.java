package com.testAssignment.demo.service;

import com.testAssignment.demo.dto.MappingUpdateUserDTO;
import com.testAssignment.demo.dto.RequestUpdateUserDTO;
import com.testAssignment.demo.model.Users;
import com.testAssignment.demo.model.validator.Address;
import com.testAssignment.demo.model.validator.Email;
import com.testAssignment.demo.model.validator.PhoneNumber;
import com.testAssignment.demo.repository.UsersRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UsersServiceTests {

    @Autowired
    @InjectMocks
    private UsersService usersService;

    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private MappingUpdateUserDTO mappingUpdateUserDTO;

    @BeforeEach
    public void setUp() {
        mappingUpdateUserDTO = new MappingUpdateUserDTO(new ModelMapper());
    }

    @Test
    public void createUser_WhenSaveUserWithValidAge_ReturnSavedUser() {

        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("John")
                .lastName("Snow")
                .email(new Email("john.show@gmail.com"))
                .birthDate(LocalDate.of(2005, 1, 1))
                .address(new Address("St. Street 123"))
                .phoneNumber(new PhoneNumber("+38(044)537-14-28"))
                .build();

        Users user = mappingUpdateUserDTO.convertToEntity(requestUpdateUserDTO);

        when(usersRepository.save(any(Users.class))).thenReturn(user);

        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Snow");
        assertThat(user.getEmail()).isEqualTo(new Email("john.show@gmail.com"));
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(2005, 1, 1).toString());
        assertThat(user.getAddress()).isEqualTo(new Address("St. Street 123"));
        assertThat(user.getPhoneNumber()).isEqualTo(new PhoneNumber("+38(044)537-14-28"));
    }

    @Test
    public void createUser_WhenUserIsUnder18_ThrowResponseStatusException() {

        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("John")
                .lastName("Snow")
                .email(new Email("john.show@gmail.com"))
                .birthDate(LocalDate.of(2010, 1, 1))
                .address(new Address("St. Street 123"))
                .phoneNumber(new PhoneNumber("+38(044)537-14-28"))
                .build();

        assertThrows(ResponseStatusException.class, () -> {
            usersService.createUser(requestUpdateUserDTO);
        });
    }

    @Test
    public void partUpdateUser_ReturnUpdatedUserFields() {
        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("Arian")
                .lastName("Internal")
                .build();

       Users updatedUserWithDTO = mappingUpdateUserDTO.convertToEntity(requestUpdateUserDTO);

        Optional<Users> getUserById = Optional.of(
                new Users(1L,
                        new Email("john.show@gmail.com"),
                        "John",
                        "Snow",
                        LocalDate.of(2000, 1, 11),
                        new Address("St. Street 34"),
                        new PhoneNumber("+38(044)537-14-28"))
        );

        when(usersRepository.findById(anyLong())).thenReturn(getUserById);
        when(usersRepository.save(any(Users.class))).thenReturn(updatedUserWithDTO);

        Users updatedUser = usersService.patchUpdateUserById(1L, requestUpdateUserDTO);

        assertEquals(requestUpdateUserDTO.getFirstName(), updatedUser.getFirstName());
        assertEquals(requestUpdateUserDTO.getLastName(), updatedUser.getLastName());
    }

    @Test
    public void totalUpdateUser_ReturnUpdatedFieldUser() {

        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("Vlad")
                .lastName("User")
                .email(new Email("vlad.user@gmail.com"))
                .birthDate(LocalDate.of(2004, 4, 21))
                .address(new Address("St. Alisveen 53"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        Users updatedUserWithDTO = new Users(
                1L,
                requestUpdateUserDTO.getEmail(),
                requestUpdateUserDTO.getFirstName(),
                requestUpdateUserDTO.getLastName(),
                requestUpdateUserDTO.getBirthDate(),
                requestUpdateUserDTO.getAddress(),
                requestUpdateUserDTO.getPhoneNumber()
        );

        Optional<Users> getUserById = Optional.of(
                new Users(1L,
                        new Email("john.show@gmail.com"),
                        "John",
                        "Snow",
                        LocalDate.of(2000, 1, 11),
                        new Address("St. Street 34"),
                        new PhoneNumber("+38(044)537-14-28"))
        );

        when(usersRepository.findById(anyLong())).thenReturn(getUserById);
        when(usersRepository.save(any(Users.class))).thenReturn(updatedUserWithDTO);

        Users updatedUser = usersService.putUpdateUserById(1L, requestUpdateUserDTO);

        assertEquals(updatedUserWithDTO.getFirstName(), updatedUser.getFirstName());
        assertEquals(updatedUserWithDTO.getLastName(), updatedUser.getLastName());
        assertEquals(updatedUserWithDTO.getEmail(), updatedUser.getEmail());
        assertEquals(updatedUserWithDTO.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(updatedUserWithDTO.getAddress(), updatedUser.getAddress());
        assertEquals(updatedUserWithDTO.getPhoneNumber(), updatedUser.getPhoneNumber());
    }

    @Test
    public void deleteUser_WhenUserWasFound_ReturnIdUser() {

        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("first")
                .lastName("last")
                .email(new Email("first.last@gmail.com"))
                .birthDate(LocalDate.of(2010, 9, 3))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        Users users = mappingUpdateUserDTO.convertToEntity(requestUpdateUserDTO);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));

        long expectedMessage = 1L;
        long actualMessage = usersService.deleteUserById(1L);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testDeleteUserById_ResponseStatusException() {
        Long id = 1L;
        doThrow(new ResponseStatusException(HttpStatus.NO_CONTENT, "User not found")).when(usersRepository).deleteById(id);

        Long result = usersService.deleteUserById(id);

        assertEquals(-1L, result);
    }

    @Test
    public void testDeleteUserById_EmptyResultDataAccessException() {
        Long id = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(usersRepository).deleteById(id);

        Long result = usersService.deleteUserById(id);

        assertEquals(-2L, result);
    }

    @Test
    public void testDeleteUserById_GenericException() {
        Long id = 1L;
        doThrow(new RuntimeException("Some error")).when(usersRepository).deleteById(id);

        Long result = usersService.deleteUserById(id);

        assertEquals(-3L, result);
    }

    @Test
    public void checkTheDateRange_ReturnUsersWithDateRange() {

        LocalDate fromDate = LocalDate.of(2000, 2, 21);
        LocalDate toDate = LocalDate.of(2013, 3, 19);

        RequestUpdateUserDTO DTO1 = RequestUpdateUserDTO.builder()
                .firstName("first")
                .lastName("last")
                .email(new Email("first.last@gmail.com"))
                .birthDate(LocalDate.of(2010, 9, 3))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        RequestUpdateUserDTO DTO2 = RequestUpdateUserDTO.builder()
                .firstName("second")
                .lastName("last")
                .email(new Email("second.last@gmail.com"))
                .birthDate(LocalDate.of(2012, 4, 21))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        Users user1 = mappingUpdateUserDTO.convertToEntity(DTO1);
        Users user2 = mappingUpdateUserDTO.convertToEntity(DTO2);
        List<Users> usersList = Arrays.asList(user1, user2);

        when(usersRepository.findUsersByBirthDate(fromDate, toDate)).thenReturn(usersList);

        List<Users> response = usersService.findUsersWithDateRange(fromDate, toDate);

        for (Users user : response) {
            LocalDate userBirthDate = user.getBirthDate();
            assertTrue(userBirthDate.isEqual(fromDate) || userBirthDate.isAfter(fromDate));
            assertTrue(userBirthDate.isEqual(toDate) || userBirthDate.isBefore(toDate));
        }

        RequestUpdateUserDTO DTO3 = RequestUpdateUserDTO.builder()
                .firstName("third")
                .lastName("last")
                .email(new Email("third.last@gmail.com"))
                .birthDate(LocalDate.of(1995, 3, 10))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        RequestUpdateUserDTO DTO4 = RequestUpdateUserDTO.builder()
                .firstName("fourth")
                .lastName("last")
                .email(new Email("fourth.last@gmail.com"))
                .birthDate(LocalDate.of(2015, 8, 5))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        Users user3 = mappingUpdateUserDTO.convertToEntity(DTO3);
        Users user4 = mappingUpdateUserDTO.convertToEntity(DTO4);

        List<Users> usersNotInRange = Arrays.asList(user3, user4);

        for (Users user : usersNotInRange) {
            assertFalse(response.contains(user));
        }
    }
}