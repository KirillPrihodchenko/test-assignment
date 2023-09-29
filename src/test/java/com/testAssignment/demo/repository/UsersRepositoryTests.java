package com.testAssignment.demo.repository;

import com.testAssignment.demo.model.Users;
import com.testAssignment.demo.model.validator.Address;
import com.testAssignment.demo.model.validator.Email;
import com.testAssignment.demo.model.validator.PhoneNumber;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsersRepositoryTests {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void UsersRepository_SaveAll_ReturnSavedUsers() {

        Users user = Users.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email(new Email("first.last@gmail.com"))
                .birthDate(LocalDate.of(2010, 9, 3))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        Users savedUser = usersRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThenOneUser() {

        Users user1 = Users.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email(new Email("first.last@gmail.com"))
                .birthDate(LocalDate.of(2010, 9, 3))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        Users user2 = Users.builder()
                .id(2L)
                .firstName("second")
                .lastName("second-last")
                .email(new Email("fsecond-last@gmail.com"))
                .birthDate(LocalDate.of(2003, 9, 3))
                .address(new Address("St. we43 2"))
                .phoneNumber(new PhoneNumber("+38(044)731-93-01"))
                .build();

        List<Users> usersList = Arrays.asList(user1, user2);

        usersRepository.saveAll(usersList);

        List<Users> findAllUsers = usersRepository.findAll();

        Assertions.assertThat(findAllUsers).isNotNull();
        Assertions.assertThat(findAllUsers.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {

        Users user = Users.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email(new Email("first.last@gmail.com"))
                .birthDate(LocalDate.of(2010, 9, 3))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        usersRepository.save(user);

        Users usersList = usersRepository.findById(user.getId()).get();

        Assertions.assertThat(usersList).isNotNull();
    }

    @Test
    public void UserRepository_UserDelete_ReturnUserIsEmpty() {

        Users user = Users.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email(new Email("first.last@gmail.com"))
                .birthDate(LocalDate.of(2010, 9, 3))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        usersRepository.save(user);

        usersRepository.deleteById(user.getId());
        Optional<Users> userReturn = usersRepository.findById(user.getId());

        Assertions.assertThat(userReturn).isEmpty();
    }

    @Test
    public void UserRepository_FindAllUserByDateRange_ReturnOptionUsers() {

        Users user1 = Users.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email(new Email("first.last@gmail.com"))
                .birthDate(LocalDate.of(2010, 9, 3))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        Users user2 = Users.builder()
                .id(2L)
                .firstName("second")
                .lastName("second-last")
                .email(new Email("fsecond-last@gmail.com"))
                .birthDate(LocalDate.of(2003, 9, 3))
                .address(new Address("St. we43 2"))
                .phoneNumber(new PhoneNumber("+38(044)731-93-01"))
                .build();

        usersRepository.saveAll(Arrays.asList(user1, user2));

        LocalDate startDate = LocalDate.of(2003, 9, 3);
        LocalDate endDate = LocalDate.of(2010, 9, 3);

        List<Users> usersList = usersRepository.findUsersByBirthDate(startDate, endDate);

        Assertions.assertThat(usersList).isNotNull();
        Assertions.assertThat(usersList.size()).isEqualTo(2);
    }
}