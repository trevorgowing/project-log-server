package com.trevorgowing.projectlog.common.builders;

import java.io.Serializable;

public interface DomainObjectBuilder<DO extends Serializable> {

    DO build();
}
