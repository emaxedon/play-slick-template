require.config
  baseUrl: '/assets'

  shim: 
    'bootstrap': deps: [ 'jquery' ]
    'ngRoute': 
      deps: [ 'angular' ]
      exports: 'angular'
    'ngCookies':
      deps: [ 'angular' ]
      exports: 'angular'
    'angular':
      deps: [ 'jquery' ]
      exports: 'angular'
    'angular-ui-bootstrap':
      deps: [ 'angular' ]

  paths:
    'jquery': 'lib/jquery/jquery.min'
    'bootstrap': 'lib/bootstrap/js/bootstrap.min'
    'angular': 'lib/angularjs/angular.min'
    'ngRoute': 'lib/angularjs/angular-route.min'
    'ngCookies': 'lib/angularjs/angular-cookies.min'
    'angular-ui-bootstrap': 'lib/angular-ui-bootstrap/ui-bootstrap-tpls.min'

require [
  'jquery'
  'javascripts/admin/app'
], ($, app) ->
  $(document).ready ->
    app.init()