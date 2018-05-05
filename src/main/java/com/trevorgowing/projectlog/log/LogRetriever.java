package com.trevorgowing.projectlog.log;

import java.util.List;

public interface LogRetriever {

  List<LogDTO> getLogDTOs();

  LogDTO getLogDTOById(long id);
}
