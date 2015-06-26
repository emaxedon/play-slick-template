define [
	'appService'
	'fileUploadService'
	'fileModel'
	'appRoute'
	'appForgotPasswordController'
	'appLoginController'
	'appDashController'
	'appUsersController'
	'appFeedsController'
	'appPopularController'
	'appTrendingController'
	'appImportExportController'
], (app) ->
	app.init = -> angular.bootstrap document, [ 'app' ]
	app
