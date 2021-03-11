package com.codexsoft.servicesupport.main.domain;

import com.codexsoft.servicesupport.main.domain.base.PersistableEntity;
import com.codexsoft.servicesupport.main.domain.type.CategoryType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "category")
public class Category extends PersistableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CategoryType type;
}
