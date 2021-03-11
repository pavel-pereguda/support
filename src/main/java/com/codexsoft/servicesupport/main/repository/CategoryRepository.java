package com.codexsoft.servicesupport.main.repository;

import com.codexsoft.servicesupport.main.domain.Category;

public interface CategoryRepository extends BaseRepository<Category> {

    Category findByName(String name);
}
