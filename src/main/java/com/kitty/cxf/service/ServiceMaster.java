package com.kitty.cxf.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "ServiceMaster", targetNamespace = "http://my.com")
public interface ServiceMaster
{
    @WebMethod
    public abstract String notice(@WebParam(name = "notice") String notice);
    
    @WebMethod
    public abstract String close(@WebParam(name = "close") String close);
    
    @WebMethod
    public abstract String speak(@WebParam(name = "speak") String speak);
    
    @WebMethod
    public abstract String seal(@WebParam(name = "seal") String seal);
    
    @WebMethod
    public abstract String coin(@WebParam(name = "coin") String coin);
    
    @WebMethod
    public abstract String recharge(@WebParam(name = "recharge") String recharge);
    
    @WebMethod
    public abstract String rechargeable(@WebParam(name = "rechargeable") String rechargeable);
    
    @WebMethod
    public abstract String prop(@WebParam(name = "prop") String prop);
    
    @WebMethod
    public abstract String gamers(@WebParam(name = "gamers") String gamers);
    
    @WebMethod
    public abstract String exp(@WebParam(name = "exp") String exp);
    
    @WebMethod
    public abstract String task(@WebParam(name = "task") String task);
    
    @WebMethod
    public abstract String inquire(@WebParam(name = "inquire") String inquire);
    
    @WebMethod
    public abstract String market(@WebParam(name = "market") String market);
    
    @WebMethod
    public abstract String load(@WebParam(name = "load") String load);
    
    @WebMethod
    public abstract String gift(@WebParam(name = "gift") String gift);
    
    @WebMethod
    public abstract String adjustRoleGrade(@WebParam(name = "adjustRoleGrade") String adjustRoleGrade);
    
    @WebMethod
    public abstract String pet(@WebParam(name = "pet") String pet);
    
    @WebMethod
    public abstract String horse(@WebParam(name = "horse") String horse);
    
    @WebMethod
    public abstract String rune(@WebParam(name = "rune") String rune);
    
    @WebMethod
    public abstract String magical(@WebParam(name = "magical") String magical);
    
    @WebMethod
    public abstract String sin(@WebParam(name = "sin") String sin);
    
    @WebMethod
    public abstract String acsry(@WebParam(name = "acsry") String acsry);
    
    @WebMethod
    public abstract String acsryPet(@WebParam(name = "acsryPet") String acsryPet);
    
    @WebMethod
    public abstract String email(@WebParam(name = "email") String email);
    
    @WebMethod
    public abstract String delEmail(@WebParam(name = "email") String email);
}
