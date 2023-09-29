package com.testAssignment.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testAssignment.demo.dto.MappingUpdateUserDTO;
import com.testAssignment.demo.dto.RequestUpdateUserDTO;
import com.testAssignment.demo.model.Users;
import com.testAssignment.demo.model.validator.Address;
import com.testAssignment.demo.model.validator.Email;
import com.testAssignment.demo.model.validator.PhoneNumber;
import com.testAssignment.demo.service.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class)
@ExtendWith(SpringExtension.class)
public class UsersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private UsersController usersController;

    @MockBean
    private UsersService usersService;

    @Test
    public void UsersController_CreateUser_ReturnCreatedStatusAndCreatedUser() throws Exception {


        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("Irtuil")
                .lastName("Albateev")
                .email(new Email("irtuil.albateev@gmail.com"))
                .birthDate(LocalDate.of(1998, 7, 25))
                .address(new Address("St. Angeriing 3D"))
                .phoneNumber(new PhoneNumber("+38(044)047-43-58"))
                .build();

        MappingUpdateUserDTO mappingUpdateUserDTO = new MappingUpdateUserDTO(new ModelMapper());
        Users createdUser = mappingUpdateUserDTO.convertToEntity(requestUpdateUserDTO);

        given(usersService.createUser(ArgumentMatchers.any(RequestUpdateUserDTO.class))).willAnswer((invocation -> invocation.getArgument(0)));

        String responseJson = mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestUpdateUserDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertNotNull(responseJson);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestUpdateUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.firstName")
                        .value(createdUser.getFirstName()))
                .andExpect(jsonPath("$.user.lastName")
                        .value(createdUser.getLastName()))
                .andExpect(jsonPath("$.user.email")
                        .value(createdUser.getEmail()))
                .andExpect(jsonPath("$.user.birthDate")
                        .value(createdUser.getBirthDate()))
                .andExpect(jsonPath("$.user.address")
                        .value(createdUser.getAddress()))
                .andExpect(jsonPath("$.user.phone")
                        .value(createdUser.getPhoneNumber()));
    }

    @Test
    public void UsersController_CreateUser_ReturnCreatedStatusAndThrowResponseStatusExceptionIfUserIsYoungerThanMinimumAge() throws Exception {
        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("Irtuil")
                .lastName("Albateev")
                .email(new Email("irtuil.albateev@gmail.com"))
                .birthDate(LocalDate.of(2000, 7, 25))
                .address(new Address("St. Angeriing 3D"))
                .phoneNumber(new PhoneNumber("+38(044)047-43-58"))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestUpdateUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void partialUpdateUser() throws Exception {

        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .lastName("Internal")
                .build();

        Users user = new Users(1L,
                new Email("john.show@gmail.com"),
                "John",
                requestUpdateUserDTO.getLastName(),
                LocalDate.of(2000, 1, 11),
                new Address("St. Street 34"),
                new PhoneNumber("+38(044)537-14-28")
        );

        given(usersService.patchUpdateUserById(anyLong(), any(RequestUpdateUserDTO.class))).willReturn(user);

        mockMvc.perform(
                patch("/users/1/partUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void totalUpdateUser() throws Exception {

        RequestUpdateUserDTO requestUpdateUserDTO = RequestUpdateUserDTO.builder()
                .firstName("Irtuil")
                .lastName("Albateev")
                .email(new Email("irtuil.albateev@gmail.com"))
                .birthDate(LocalDate.of(1998, 7, 25))
                .address(new Address("St. Angeriing 3D"))
                .phoneNumber(new PhoneNumber("+38(044)047-43-58"))
                .build();

        Users user = new Users(1L,
                requestUpdateUserDTO.getEmail(),
                requestUpdateUserDTO.getFirstName(),
                requestUpdateUserDTO.getLastName(),
                requestUpdateUserDTO.getBirthDate(),
                requestUpdateUserDTO.getAddress(),
                requestUpdateUserDTO.getPhoneNumber()
        );

        given(usersService.putUpdateUserById(anyLong(), any(RequestUpdateUserDTO.class))).willReturn(user);

        mockMvc.perform(
                        put("/users/1/totalUpdate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserByIdSuccess() {
        Long userId = 1L;
        when(usersService.deleteUserById(userId)).thenReturn(userId);

        Long result = usersService.deleteUserById(userId);

        assertEquals(userId, result);
    }

    @Test
    public void getAllUsersWithBirthDateRange_ReturnGetStatusOk() throws Exception {

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
                .lastName("last")
                .email(new Email("second.last@gmail.com"))
                .birthDate(LocalDate.of(2012, 4, 21))
                .address(new Address("St. Alasi 2"))
                .phoneNumber(new PhoneNumber("+38(044)431-93-01"))
                .build();

        List<Users> usersList = Arrays.asList(user1, user2);

        given(usersService.findUsersWithDateRange(user1.getBirthDate(), user2.getBirthDate())).willReturn(usersList);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/")
                                .param("fromDate", "2023-09-01")
                                .param("toDate", "2023-09-30")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("first"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("second"));
    }
}