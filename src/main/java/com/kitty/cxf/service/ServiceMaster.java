
package com.kitty.cxf.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "ServiceMaster", targetNamespace = "http://my.com")
public interface ServiceMaster
{
    @WebMethod
    String notice(@WebParam(name = "notice") final String p0);
    
    @WebMethod
    String close(@WebParam(name = "close") final String p0);
    
    @WebMethod
    String speak(@WebParam(name = "speak") final String p0);
    
    @WebMethod
    String seal(@WebParam(name = "seal") final String p0);
    
    @WebMethod
    String coin(@WebParam(name = "coin") final String p0);
    
    @WebMethod
    String recharge(@WebParam(name = "recharge") final String p0);
    
    @WebMethod
    String rechargeable(@WebParam(name = "rechargeable") final String p0);
    
    @WebMethod
    String prop(@WebParam(name = "prop") final String p0);
    
    @WebMethod
    String gamers(@WebParam(name = "gamers") final String p0);
    
    @WebMethod
    String exp(@WebParam(name = "exp") final String p0);
    
    @WebMethod
    String task(@WebParam(name = "task") final String p0);
    
    @WebMethod
    String inquire(@WebParam(name = "inquire") final String p0);
    
    @WebMethod
    String market(@WebParam(name = "market") final String p0);
    
    @WebMethod
    String load(@WebParam(name = "load") final String p0);
    
    @WebMethod
    String gift(@WebParam(name = "gift") final String p0);
    
    @WebMethod
    String adjustRoleGrade(@WebParam(name = "adjustRoleGrade") final String p0);
    
    @WebMethod
    String pet(@WebParam(name = "pet") final String p0);
    
    @WebMethod
    String horse(@WebParam(name = "horse") final String p0);
    
    @WebMethod
    String rune(@WebParam(name = "rune") final String p0);
    
    @WebMethod
    String magical(@WebParam(name = "magical") final String p0);
    
    @WebMethod
    String sin(@WebParam(name = "sin") final String p0);
    
    @WebMethod
    String acsry(@WebParam(name = "acsry") final String p0);
    
    @WebMethod
    String acsryPet(@WebParam(name = "acsryPet") final String p0);
    
    @WebMethod
    String email(@WebParam(name = "email") final String p0);
    
    @WebMethod
    String delEmail(@WebParam(name = "email") final String p0);
}
