package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM categories";
        return jdbcTemplate.query(sql, (row, rowNum) -> mapRow(row));
    }

    // get category by id
    @Override

    public Category getById(int categoryId) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (row, rowNum) -> mapRow(row), categoryId);

        } catch (Exception e) {
            return null;
        }
    }

    // create a new category
    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories(name,description) VALUES(?,?)";
        jdbcTemplate.update(sql, category.getName(), category.getDescription());
        Integer newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        category.setCategoryId(newId);

        return category;
    }

    // update category
    @Override
    public void update(int categoryId, Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        jdbcTemplate.update(sql, category.getName(), category.getDescription(), categoryId);
    }


    @Override
    public void delete(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        jdbcTemplate.update(sql, categoryId);
    }


    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
