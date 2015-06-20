define [ 'appModule' ], (app) ->
	app.controller 'DashController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		service.loggedin()
		$http.get('/auth/recent/10')
			.success (data, status, headers, config) ->
				if data.result == 1
					$scope.users = data.data.users
					
			.error (data, status, headers, config) ->
				
		$http.get('/auth/count')
			.success (data, status, headers, config) ->
				if data.result == 1
					$scope.userCount = data.data.count
		$http.get('/feeds/recent/10')
			.success (data, status, headers, config) ->
				if data.result == 1
					$scope.feeds = data.data.feeds
				
		$http.get('/feeds/count')
			.success (data, status, headers, config) ->
				if data.result == 1
					$scope.feedCount = data.data.count
				
		$scope.logout = service.logout
		]