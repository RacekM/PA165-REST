package cz.fi.muni.pa165.rest.controllers;

import cz.fi.muni.pa165.dto.NewPriceDTO;
import cz.fi.muni.pa165.dto.ProductCreateDTO;
import cz.fi.muni.pa165.dto.ProductDTO;
import cz.fi.muni.pa165.exceptions.EshopServiceException;
import cz.fi.muni.pa165.facade.ProductFacade;
import cz.fi.muni.pa165.rest.ApiUris;
import cz.fi.muni.pa165.rest.exceptions.InvalidParameterException;
import cz.fi.muni.pa165.rest.exceptions.ResourceAlreadyExistingException;
import cz.fi.muni.pa165.rest.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * REST Controller for Products
 * <p>
 * TODO: Annotate the controller with @RestController and add a
 * RequestMapping using ApiUris.ROOT_URI_PRODUCTS
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_PRODUCTS)
public class ProductsController {

    final static Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Inject
    private ProductFacade productFacade;

    /**
     * Get list of Products curl -i -X GET
     * http://localhost:8080/eshop-rest/products
     * <p>
     * TODO:
     * 1. annotate the method to have as method RequestMethod.GET
     * and producing application/json
     * 2. return a list of ProductDTOs
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<ProductDTO> getProducts() {
        return productFacade.getAllProducts();
    }

    /**
     * Get Product by identifier id curl -i -X GET
     * http://localhost:8080/eshop-rest/products/1
     * <p>
     * <p>
     * TODO:
     * 1. add the mapping using the resource id
     * 2. retrieve the id from the request by using pathvariable
     * 3. you can return HttpStatus.NOT_FOUND 404 if the product is not found
     * (note: at the current time there is an issue at the persistence layer
     * https://github.com/fi-muni/PA165/issues/28
     * so using the facade to get one product with a non-existing id will throw
     * a Dozer exception - you can catch the exception and re-throw it )
     * You can use the class ResourceNotFoundException
     */
    @RequestMapping(value = "{/product_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ProductDTO getProduct(@PathVariable("product_id") long product_id) throws Exception {
        ProductDTO productDTO = productFacade.getProductWithId(product_id);
        if(productDTO != null) {
            return productDTO;
        }
        throw new ResourceNotFoundException();
    }

    /**
     * Delete one product by id curl -i -X DELETE
     * http://localhost:8080/eshop-rest/products/1
     * <p>
     * TODO:
     * 1. delete one product identified by id
     * 2. what about deleting the same element multiple times?
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteProduct(@PathVariable("id") long id) throws Exception {
        try {
            productFacade.deleteProduct(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Create a new product by POST method
     * curl -X POST -i -H "Content-Type: application/json" --data
     * '{"name":"test","description":"test","color":"UNDEFINED","price":"200",
     * "currency":"CZK", "categoryId":"1"}'
     * http://localhost:8080/eshop-rest/products/create
     * <p>
     * TODO:
     * 1. use the requestbody annotation to get access to the request body
     * 2. use the ProductCreateDTO for the creation of a new product, but return a ProductDTO
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final ProductDTO createProduct(@RequestBody ProductCreateDTO product) throws Exception {
        try {
            Long id = productFacade.createProduct(product);
            return productFacade.getProductWithId(id);
        } catch (Exception ex) {
            throw new ResourceAlreadyExistingException();
        }
    }

    /**
     * Update the price for one product by PUT method curl -X PUT -i -H
     * "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}'
     * http://localhost:8080/eshop-rest/products/4
     * <p>
     * TODO: Update the price for one product (you need to use  NewPriceDTO)
     * take care also about the price validation at the service layer
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final ProductDTO changePrice(@PathVariable("id") long id, @RequestBody NewPriceDTO newPrice) throws Exception {
        try {
            newPrice.setProductId(id);
            productFacade.changePrice(newPrice);
            return productFacade.getProductWithId(id);
        } catch (EshopServiceException esse) {
            throw new InvalidParameterException();
        }
    }


}
