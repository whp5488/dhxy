package com.kitty.cxf.model;

import java.io.Serializable;

public class Inquire implements Serializable {
  private long id;
  
  private int r;
  
  private String n;
  
  private int no;
  
  private byte re;
  
  private short gr;
  
  private String xy;
  
  private String yl;
  
  private String total;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setR(int r) {
    this.r = r;
  }
  
  public void setN(String n) {
    this.n = n;
  }
  
  public void setNo(int no) {
    this.no = no;
  }
  
  public void setRe(byte re) {
    this.re = re;
  }
  
  public void setGr(short gr) {
    this.gr = gr;
  }
  
  public void setXy(String xy) {
    this.xy = xy;
  }
  
  public void setYl(String yl) {
    this.yl = yl;
  }
  
  public void setTotal(String total) {
    this.total = total;
  }
  
  public long getId() {
    return this.id;
  }
  
  public int getR() {
    return this.r;
  }
  
  public String getN() {
    return this.n;
  }
  
  public int getNo() {
    return this.no;
  }
  
  public byte getRe() {
    return this.re;
  }
  
  public short getGr() {
    return this.gr;
  }
  
  public String getXy() {
    return this.xy;
  }
  
  public String getYl() {
    return this.yl;
  }
  
  public String getTotal() {
    return this.total;
  }
  
  public Inquire() {}
  
  public Inquire(long id, int r, String n, int no, byte re, short gr, String xy, String yl, String total) {
    this.id = id;
    this.r = r;
    this.n = n;
    this.no = no;
    this.re = re;
    this.gr = gr;
    this.xy = xy;
    this.yl = yl;
    this.total = total;
  }
}
