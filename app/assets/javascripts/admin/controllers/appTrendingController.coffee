define [ 'appModule' ], (app) ->
	app.controller 'TrendingController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.feeds = []
		$scope.error = false
		$scope.message = false
		service.loggedin()

		trending = ->
			$http.get '/feeds/trending'
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.feeds = data.data.feeds

		$scope.remove = (id) ->
			$scope.error = false
			$scope.message = false

			$http.get '/feeds/trending/remove/' + id
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.message = "Successfully removed feed from trending feeds."
						trending()
		
		$scope.add = ->
			$scope.error = false
			$scope.message = false

			$http.get '/feeds/trending/add/' + $scope.feedId
				.success (data, status, headers, config) ->
					$scope.message = "Successfully added feed to trending feeds."
					trending()

		$scope.logout = service.logout

		trending()
		]