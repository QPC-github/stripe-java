package com.stripe.model;

import com.stripe.Stripe;
import com.stripe.exception.ApiKeyMissingException;
import com.stripe.net.RequestOptions;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Provides a representation of a single page worth of data from a Stripe API search method. */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public abstract class StripeSearchResult<T> extends StripeObject
    implements StripeSearchResultInterface<T> {
  String object;

  @Getter(onMethod_ = {@Override})
  List<T> data;

  /**
   * Get the total count of search records in the result. The value is present when `total_count` is
   * added to the `expand` search parameter.
   */
  @Getter(onMethod_ = {@Override})
  Long totalCount;

  @Getter(onMethod_ = {@Override})
  Boolean hasMore;

  @Getter(onMethod_ = {@Override})
  String url;

  @Getter(onMethod_ = {@Override})
  String nextPage;

  @Getter(onMethod_ = {@Override})
  @Setter(onMethod = @__({@Override}))
  private transient RequestOptions requestOptions;

  @Getter(onMethod_ = {@Override})
  @Setter(onMethod = @__({@Override}))
  private Map<String, Object> requestParams;

  public Iterable<T> autoPagingIterable() {
    if (Stripe.apiKey == null
        && (this.requestOptions == null || this.requestOptions.getApiKey() == null)) {
      throw new ApiKeyMissingException(
          "API key is not set for autoPagingIterable. You can set the API key globally using Stripe.ApiKey, or through RequestOptions with autoPagingIterable(params, options).");
    }
    return new SearchPagingIterable<>(this);
  }

  public Iterable<T> autoPagingIterable(Map<String, Object> params) {
    if (Stripe.apiKey == null
        && (this.requestOptions == null || this.requestOptions.getApiKey() == null)) {
      throw new ApiKeyMissingException(
          "API key is not set for autoPagingIterable. You can set the API key globally using Stripe.ApiKey, or through RequestOptions with autoPagingIterable(params, options).");
    }
    this.setRequestParams(params);
    return new SearchPagingIterable<>(this);
  }

  /**
   * Constructs an iterable that can be used to iterate across all objects across all pages. As page
   * boundaries are encountered, the next page will be fetched automatically for continued
   * iteration.
   *
   * @param params request parameters (will override the parameters from the initial list request)
   * @param options request options (will override the options from the initial list request)
   */
  public Iterable<T> autoPagingIterable(Map<String, Object> params, RequestOptions options) {
    String apiKey = options.getApiKey();
    if (Stripe.apiKey == null && apiKey == null) {
      throw new ApiKeyMissingException(
          "API key is not set for autoPagingIterable. You can set the API key globally using Stripe.ApiKey, or through RequestOptions with autoPagingIterable(params, options).");
    }
    this.setRequestOptions(options);
    this.setRequestParams(params);
    return new SearchPagingIterable<>(this);
  }
}
