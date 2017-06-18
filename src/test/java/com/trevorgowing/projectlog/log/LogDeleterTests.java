package com.trevorgowing.projectlog.log;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class LogDeleterTests extends AbstractTests {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogDeleter logDeleter;

    @Test(expected = LogNotFoundException.class)
    public void testDeleteLogByIdWithNoExistingLog_shouldThrowLogNotFoundException() {
        // Set up expectations
        doThrow(new EmptyResultDataAccessException(1)).when(logRepository).delete(1L);

        // Exercise SUT
        logDeleter.deleteLogById(1L);
    }

    @Test
    public void testDeleteLogByIdWithExistingLog_shouldDelegateToLogRepositoryToDeleteLog() throws Exception {
        // Set up expectations
        doNothing().when(logRepository).delete(1L);

        // Exercise SUT
        logDeleter.deleteLogById(1L);
    }
}