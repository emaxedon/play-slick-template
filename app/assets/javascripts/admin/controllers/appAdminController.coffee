define [ 'appModule' ], (app) ->

	app.controller 'AdminController', ['$scope', '$http', 'service', ($scope, $http, service) ->
		$scope.logout = service.logout

		]