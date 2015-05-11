package org.jingfu.order.bean;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean; 
   // or import javax.inject.Named;
import javax.faces.bean.SessionScoped; 
   // or import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean // or @Named
@SessionScoped
public class LocaleChanger implements Serializable {
   public String chineseAction() {
      FacesContext context = FacesContext.getCurrentInstance();
      context.getViewRoot().setLocale(Locale.CHINESE);
      return null;
   }
   
   public String englishAction() {
      FacesContext context = FacesContext.getCurrentInstance();
      context.getViewRoot().setLocale(Locale.ENGLISH);
      return null;
   }
}
