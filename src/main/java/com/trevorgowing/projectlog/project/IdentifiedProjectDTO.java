package com.trevorgowing.projectlog.project;

import com.trevorgowing.projectlog.user.IdentifiedUserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

import static com.trevorgowing.projectlog.user.IdentifiedUserDTO.passwordFreeIdentifiedUserDTO;

@NoArgsConstructor
@Getter
@Setter
public class IdentifiedProjectDTO extends UnidentifiedProjectDTO {

    private static final long serialVersionUID = 5757020105419667746L;

    private long id;

    private IdentifiedProjectDTO(long id, String code, String name, IdentifiedUserDTO owner, LocalDate startDate,
                                 LocalDate endDate) {
        super(code, name, owner, startDate, endDate);
        this.id = id;
    }

    private IdentifiedProjectDTO(long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    /**
     * Used in constructor query {@link ProjectRepository#findIdentifiedProjectDTOs()}.
     */
    @SuppressWarnings("unused")
    public IdentifiedProjectDTO(long id, String code, String name, LocalDate startDate, LocalDate endDate,
                                long ownerId, String ownerEmail, String ownerFirstName, String ownerLastName) {
        super(code, name, passwordFreeIdentifiedUserDTO(ownerId, ownerEmail, ownerFirstName, ownerLastName), startDate,
                endDate);
        this.id = id;
    }

    public static IdentifiedProjectDTO completeIdentifiedProjectDTO(
            long id, String code, String name, IdentifiedUserDTO owner, LocalDate startDate, LocalDate endDate) {
        return new IdentifiedProjectDTO(id, code, name, owner, startDate, endDate);
    }

    public static IdentifiedProjectDTO minimalIdentifiedProjectDTO(long id, String code, String name) {
        return new IdentifiedProjectDTO(id, code, name);
    }

    @Override
    public boolean equals(Object objectToCompareTo) {
        if (this == objectToCompareTo) return true;
        if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
        if (!super.equals(objectToCompareTo)) return false;
        IdentifiedProjectDTO identifiedProjectDTOToCompareTo = (IdentifiedProjectDTO) objectToCompareTo;
        return id == identifiedProjectDTOToCompareTo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName());
        toStringBuilder.append("{ id=").append(id);
        toStringBuilder.append(", code='").append(code).append('\'');
        toStringBuilder.append(", name='").append(name).append('\'');
        toStringBuilder.append(", owner=").append(owner);
        toStringBuilder.append(", startDate=").append(startDate);
        toStringBuilder.append(", endDate=").append(endDate);
        toStringBuilder.append('}');
        return toStringBuilder.toString();
    }
}