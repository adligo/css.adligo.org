package org.adligo.css.client.obtain;

import org.adligo.css.shared.models.I_StyleSheet;

/**
 * Note I use the term requester 
 * to distinguish it from a 
 * service since it is a client side
 * only interface (in other words no servlet).
 * 
 * @author scott
 *
 */
public interface I_CssRequester {
  public void onSuccess(I_StyleSheet stylesheet);
  public void onFailure(Throwable thrown);
}
