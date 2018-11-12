package cz.fi.muni.pa165.rest.controllers;

import cz.fi.muni.pa165.dto.ProductDTO;
import cz.fi.muni.pa165.facade.ProductFacade;
import cz.fi.muni.pa165.rest.ApiUris;
import cz.fi.muni.pa165.rest.assemblers.ProductResourceAssembler;
import cz.fi.muni.pa165.rest.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/products_hateoas")
public class ProductsControllerHateoas {

    final static Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Inject
    private ProductFacade productFacade;

    @Inject
    private ProductResourceAssembler productResourceAssembler;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final HttpEntity<Resources<Resource<ProductDTO>>> getProducts() {

        Collection<ProductDTO> productsDTO = productFacade.getAllProducts();
        Collection<Resource<ProductDTO>> productResourceCollection = new ArrayList<>();

        for (ProductDTO p : productsDTO) {
            productResourceCollection.add(productResourceAssembler.toResource(p));
        }

        Resources<Resource<ProductDTO>> productsResources = new Resources<>(productResourceCollection);
        productsResources.add(linkTo(ProductsControllerHateoas.class).withSelfRel());

        return new ResponseEntity<>(productsResources, HttpStatus.OK);

    }
    /**
     *
     * Get Product by identifier id curl -i -X GET
     * http://localhost:8080/eshop-rest/products/1
     *
     * @param id identifier for a product
     * @return ProductDTO
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final HttpEntity<Resource<ProductDTO>> getProduct(@PathVariable("id") long id) throws Exception {

        try {
            ProductDTO productDTO = productFacade.getProductWithId(id);
            Resource<ProductDTO> resource = productResourceAssembler.toResource(productDTO);
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (Exception ex){
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Delete one product by id curl -i -X DELETE
     * http://localhost:8080/eshop-rest/products/1
     *
     * @param id identifier for product
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteProduct(@PathVariable("id") long id) throws Exception {
        logger.debug("rest deleteProduct({}) hateoas", id);
        try {
            productFacade.deleteProduct(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

}
