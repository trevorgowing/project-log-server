package com.trevorgowing.projectlog.user;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class IdentifiedUserDTO extends UnidentifiedUserDTO {

    private static final long serialVersionUID = -84805295469064090L;

    private long id;

    public IdentifiedUserDTO(long id, String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
        this.id = id;
    }

    public static IdentifiedUserDTO completeIdentifiedUserDTO(long id, String email, String password, String firstName,
                                                       String lastName) {
        return new IdentifiedUserDTO(id, email, password, firstName, lastName);
    }

    @Override
    public boolean equals(Object objectToCompareTo) {
        if (this == objectToCompareTo) return true;
        if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
        if (!super.equals(objectToCompareTo)) return false;
        IdentifiedUserDTO identifiedUserDTOToCompareTo = (IdentifiedUserDTO) objectToCompareTo;
        return id == identifiedUserDTOToCompareTo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName());
        toStringBuilder.append("{ id=").append(id);
        toStringBuilder.append(", email='").append(email).append('\'');
        toStringBuilder.append(", password='").append(password).append('\'');
        toStringBuilder.append(", firstName='").append(firstName).append('\'');
        toStringBuilder.append(", lastName='").append(lastName).append('\'');
        toStringBuilder.append('}');
        return toStringBuilder.toString();
    }
}
