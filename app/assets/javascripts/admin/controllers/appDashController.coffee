define [ 'appModule' ], (app) ->
	app.controller 'DashController', ['$scope', '$http', 'service', ($scope, $http, service) ->
		service.loggedin()
		$http.get('/auth/recent/10')
			.success (data, status, headers, config) ->
				if data != null
					$scope.users = data.data.users
					
			.error (data, status, headers, config) ->
				
		$http.get('/auth/count')
			.success (data, status, headers, config) ->
				if data != null
					$scope.userCount = data.data.count
		]