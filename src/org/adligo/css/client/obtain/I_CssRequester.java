package org.adligo.css.client.obtain;

import org.adligo.css.shared.models.I_StyleSheet;

import java.util.List;

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
  public void onSuccess(I_StyleSheet stylesheet, List<Throwable> parseWarnings);
  public void onFailure(Throwable thrown);
}
