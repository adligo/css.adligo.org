package org.adligo.css.shared.models;

/**
 * the type of the css content,
 * (i.e. px must be a px setting not a percent.
 * any can be any kind of content.
 * 
 * @author scott
 */
public enum CssType {
 /**
  * expects a double 
  * for a percentage i.e.;
  * height: 12.5%
  */
 PCT, 
 /**
  * expects a positive integer 
  * pixel number i.e.;
  * height: 12px
  */
 PX, 
 /**
  * expects a integer i.e.;
  * height: 12
  */
 INTEGER,
 /**
  * expects a double i.e.;
  * height: 12.22
  */
 DOUBLE, 
 /**
  * turns the value into a string,
  * if further parsing is needed it must be
  * done by the code which uses gwt_css.adligo.org
  */
 ANY
}
