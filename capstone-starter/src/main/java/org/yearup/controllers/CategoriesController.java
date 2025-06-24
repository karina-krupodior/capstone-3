package org.yearup.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller
@RestController

// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
@RequestMapping("/categories")

// add annotation to allow cross site origin requests
@CrossOrigin
public class CategoriesController
{
    // create an Autowired controller to inject the categoryDao and ProductDao
    private CategoryDao categoryDao;
    private ProductDao productDao;

    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping
    // add the appropriate annotation for a get action
    public List<Category> getAll()
    {
        // find and return all categories
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id)
    {
        // get the category by id
        Category category = categoryDao.getById(id);

        if(category == null) {
            // return 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // return 200 OK with the category
        return ResponseEntity.ok(category);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    @PostMapping

    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        // insert the category
        Category created = categoryDao.create(category);


        return new ResponseEntity<>(created, HttpStatus.CREATED); // return 201 Created
    }




    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    @PutMapping("/{id}")

    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        Category existing = categoryDao.getById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build(); // return 404 if category not found
        }

        category.setCategoryId(id);
        categoryDao.update(id, category);


        return ResponseEntity.ok(category); // return 200 OK
    }

    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    @DeleteMapping("/{id}")

    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        Category existing = categoryDao.getById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build(); // return 404 if not found
        }

        // delete the category by id
        categoryDao.delete(id);

        return ResponseEntity.noContent().build(); // return 204 No Content
    }
}
