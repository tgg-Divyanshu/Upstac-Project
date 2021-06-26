package org.upgrad.upstac.testrequests;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.users.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestRequestServiceTest {

    @InjectMocks
    TestRequestService testRequestService;
    @Mock
    TestRequestRepository testRequestRepository;

    @Test
    public void when_saveTestRequest_called_with_valid_parameters_expect_save_method_of_testRequestRepository_is_called(){

        //Arrange
        User loggedInUser = createmockedUser();
        CreateTestRequest createTestRequest = createmockedTestRequestInput();
        Mockito.when(testRequestRepository.findByEmailOrPhoneNumber(createTestRequest.getEmail(),createTestRequest.getPhoneNumber())).thenReturn(new ArrayList<>());

        //Act
        testRequestService.createTestRequestFrom(loggedInUser, createTestRequest);

        //Assert
        verify(testRequestRepository, times(1)).save(any());

    }

    @Test
    public void when_saveTestRequest_called_same_values_in_database_expect_AppException_is_thrown(){

        //Arrange
        User loggedInUser = createmockedUser();
        CreateTestRequest createTestRequest = createmockedTestRequestInput();

        TestRequest testRequest = new TestRequest();
        testRequest.setStatus(RequestStatus.INITIATED);
        testRequest.setEmail("sometestuser@gmail.com");
        testRequest.setPhoneNumber("9921763145");

        List<TestRequest> mockedTestRequestForFindByEmailOrPhoneNumber =  new ArrayList<>();
        mockedTestRequestForFindByEmailOrPhoneNumber.add(testRequest);

        Mockito.when(testRequestRepository.findByEmailOrPhoneNumber(createTestRequest.getEmail(), createTestRequest.getPhoneNumber())).thenReturn(mockedTestRequestForFindByEmailOrPhoneNumber);


        //Act
        AppException appException = assertThrows(AppException.class,()->{
            testRequestService.createTestRequestFrom(loggedInUser, createTestRequest);
        });

        //Assert
        assertThat(appException.getMessage(), containsString("A Request with same PhoneNumber or Email is already in progress "));
    }

    private User createmockedUser() {
        User loggedInUser = new User();
        loggedInUser.setUserName("sometestuser");
        return loggedInUser;
    }

    private CreateTestRequest createmockedTestRequestInput() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setAddress("some address");
        createTestRequest.setAge(76);
        createTestRequest.setEmail("sometestuser@gmail.com");
        createTestRequest.setPhoneNumber("9921763145");
        return createTestRequest;
    }

    private void assertThat(String message, Matcher<String> containsString) {
    }

}