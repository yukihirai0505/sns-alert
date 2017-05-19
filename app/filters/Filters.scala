package filters

/**
  * ========================================================================
  * DEFAULT SCOPE
  * The request method is not GET, HEAD or OPTIONS.
  * The request has one or more Cookie or Authorization headers.
  * The CORS filter is not configured to trust the request’s origin.
  *
  * ------------------------------------------------------------------------
  * INTRODUCTION
  * https://www.playframework.com/documentation/2.5.x/Migration25#CSRF-changes
  *
  * ------------------------------------------------------------------------
  * DETAILS
  * https://www.playframework.com/documentation/2.5.x/ScalaCsrf
  *
  * ========================================================================
  **/

import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import play.filters.csrf.CSRFFilter

class Filters @Inject()(csrfFilter: CSRFFilter) extends DefaultHttpFilters(csrfFilter)
