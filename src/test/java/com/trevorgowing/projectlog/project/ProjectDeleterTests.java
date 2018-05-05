package com.trevorgowing.projectlog.project;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.trevorgowing.projectlog.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

public class ProjectDeleterTests extends AbstractTests {

  @Mock private ProjectRepository projectRepository;

  @InjectMocks private ProjectDeleter projectDeleter;

  @Test(expected = ProjectNotFoundException.class)
  public void testDeleteProjectWithNoMatchingProject_shouldThrowProjectNotFoundException() {
    // Set up expectations
    doThrow(new EmptyResultDataAccessException(1)).when(projectRepository).delete(1L);

    // Exercise SUT
    projectDeleter.deleteProject(1L);
  }

  @Test
  public void
      testDeleteProjectWithMatchingProject_shouldDelegateToProjectRepositoryToDeleteProject() {
    // Set up expectations
    doNothing().when(projectRepository).delete(1L);

    // Exercise SUT
    projectDeleter.deleteProject(1L);
  }
}
