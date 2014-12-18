package org.adligo.css.client.obtain;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import org.adligo.css.shared.models.I_ExpectedCss;

public class CssObtainer implements RequestCallback {
  public static final String ALREADY_PROCESSING_REQUEST = "Already processing request.";
  private I_CssRequester requester_;
  private I_ExpectedCss expectedCss_;

  public void obtainCss(String url, I_ExpectedCss expected, I_CssRequester requester) {
    if (requester_ != null) {
      throw new IllegalStateException(ALREADY_PROCESSING_REQUEST);
    }
    expectedCss_ = expected;
    RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,url);
    rb.setCallback(this);
  }
  
  public void obtainCss(String url, I_CssRequester requester) {
    if (requester_ != null) {
      throw new IllegalStateException(ALREADY_PROCESSING_REQUEST);
    }
    RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,url);
    rb.setCallback(this);
  }

  @Override
  public void onResponseReceived(Request request, Response response) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void onError(Request request, Throwable exception) {
    requester_.onFailure(exception);
    requester_ = null;
  }
}
