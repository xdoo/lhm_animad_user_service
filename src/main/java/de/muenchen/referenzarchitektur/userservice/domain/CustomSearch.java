/*
* Copyright (c): it@M - Dienstleister fuer Informations- und Telekommunikationstechnik
* der Landeshauptstadt Muenchen, 2018
 */
package de.muenchen.referenzarchitektur.userservice.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.Size;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

/**
 * This class represents a CustomSearch.
 */
@Entity
@Table(name = "customsearch")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomSearch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // ========= //
    // Variables //
    // ========= //
    @Column(name = "username", nullable = false, length = 50)
    @NotNull
    @Size(min = 0, max = 30)
    private String userName;

    @Column(name = "componentname", nullable = false, length = 50)
    @NotNull
    @Size(min = 0, max = 30)
    private String componentName;

    @Column(name = "searchname", nullable = false, length = 30)
    @NotNull
    @Size(min = 0, max = 30)
    private String searchName;

    @Column(name = "search", nullable = false, length = 200)
    @NotNull
    @Size(min = 0, max = 30)
    private String search;

    @Column(name = "lastupdated", nullable = false)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Convert(converter = Jsr310JpaConverters.LocalTimeConverter.class)
    @NotNull
    private java.time.LocalTime lastUpdated;

}
