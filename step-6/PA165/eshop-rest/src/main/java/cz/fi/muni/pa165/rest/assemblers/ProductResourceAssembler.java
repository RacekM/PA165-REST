package cz.fi.muni.pa165.rest.assemblers;

import cz.fi.muni.pa165.dto.ProductDTO;
import cz.fi.muni.pa165.rest.controllers.ProductsControllerHateoas;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class ProductResourceAssembler implements ResourceAssembler<ProductDTO,
        Resource<ProductDTO>> {

    public Resource<ProductDTO> toResource(ProductDTO productDTO) {
        long id = productDTO.getId();
        Resource<ProductDTO> productResource = new Resource<ProductDTO>(productDTO);

        try {
            productResource.add(linkTo(ProductsControllerHateoas.class).slash(productDTO.getId()).withSelfRel());
            productResource.add(linkTo(ProductsControllerHateoas.class).slash(productDTO.getId()).withRel("DELETE"));

        } catch (Exception ex) {
            Logger.getLogger(ProductResourceAssembler.class.getName()).log(Level.SEVERE, "could not link resource from ProductsControllerHateoas", ex);
        }

        return productResource;
    }
}