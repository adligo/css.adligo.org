package org.adligo.css.client.obtain;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import org.adligo.css.shared.models.I_ExpectedCss;
import org.adligo.css.shared.models.I_StyleSheet;
import org.adligo.css.shared.models.StyleSheetParser;
import org.adligo.css.shared.models.common.CssI18nConstants;

public class CssObtainer implements RequestCallback {
  public static final String ALREADY_PROCESSING_REQUEST = "Already processing request.";
  private StyleSheetParser parser_ = new StyleSheetParser();
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
    requester_ = requester;
    RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,url);
    rb.setCallback(this);
    Request request = null;
    try {
      request = rb.send();
    } catch (RequestException e) {
     onError(request, e);
    }
  }

  @Override
  public void onResponseReceived(Request request, Response response) {
    int statusCode = response.getStatusCode();
    if (statusCode != 200) {
      requester_.onFailure(new Exception("Recieved response status code " + statusCode));
    } else {
      String text = response.getText();
      I_StyleSheet styleSheet = parser_.parse(text, expectedCss_, CssI18nConstants.INSTANCE);
      requester_.onSuccess(styleSheet);
    }
  }

  @Override
  public void onError(Request request, Throwable exception) {
    requester_.onFailure(exception);
    requester_ = null;
  }
}
