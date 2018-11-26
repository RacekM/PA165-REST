package cz.fi.muni.pa165.ws;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER, faultStringOrReason = "Product not found." )
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String productName) {
        super(String.format("Product cant be found: %s", productName));
    }
}
