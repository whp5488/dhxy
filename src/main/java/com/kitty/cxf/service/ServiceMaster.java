package com.kitty.cxf.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "ServiceMaster", targetNamespace = "http://my.com")
public interface ServiceMaster {
  @WebMethod
  String notice(@WebParam(name = "notice") String paramString);
  
  @WebMethod
  String close(@WebParam(name = "close") String paramString);
  
  @WebMethod
  String speak(@WebParam(name = "speak") String paramString);
  
  @WebMethod
  String seal(@WebParam(name = "seal") String paramString);
  
  @WebMethod
  String coin(@WebParam(name = "coin") String paramString);
  
  @WebMethod
  String recharge(@WebParam(name = "recharge") String paramString);
  
  @WebMethod
  String rechargeable(@WebParam(name = "rechargeable") String paramString);
  
  @WebMethod
  String prop(@WebParam(name = "prop") String paramString);
  
  @WebMethod
  String gamers(@WebParam(name = "gamers") String paramString);
  
  @WebMethod
  String exp(@WebParam(name = "exp") String paramString);
  
  @WebMethod
  String task(@WebParam(name = "task") String paramString);
  
  @WebMethod
  String inquire(@WebParam(name = "inquire") String paramString);
  
  @WebMethod
  String market(@WebParam(name = "market") String paramString);
  
  @WebMethod
  String load(@WebParam(name = "load") String paramString);
  
  @WebMethod
  String gift(@WebParam(name = "gift") String paramString);
  
  @WebMethod
  String adjustRoleGrade(@WebParam(name = "adjustRoleGrade") String paramString);
  
  @WebMethod
  String pet(@WebParam(name = "pet") String paramString);
  
  @WebMethod
  String horse(@WebParam(name = "horse") String paramString);
  
  @WebMethod
  String rune(@WebParam(name = "rune") String paramString);
  
  @WebMethod
  String magical(@WebParam(name = "magical") String paramString);
  
  @WebMethod
  String sin(@WebParam(name = "sin") String paramString);
  
  @WebMethod
  String acsry(@WebParam(name = "acsry") String paramString);
  
  @WebMethod
  String acsryPet(@WebParam(name = "acsryPet") String paramString);
  
  @WebMethod
  String email(@WebParam(name = "email") String paramString);
  
  @WebMethod
  String delEmail(@WebParam(name = "email") String paramString);
}
