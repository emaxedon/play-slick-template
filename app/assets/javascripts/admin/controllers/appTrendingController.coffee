define [ 'appModule' ], (app) ->
	app.controller 'TrendingController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.feeds = []
		$scope.clear()
		service.loggedin()

		trending = ->
			$http.get '/feeds/trending'
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.feeds = data.data.feeds

		$scope.search = (val) ->
			$http.get '/feeds/search',
				params:
					q: val
			.then (response) ->
				response.data.data.feeds.map( (item) -> item )

		$scope.onSearchSelect = ($item, $model, $label) ->
			$scope.feedId = $item.id

		$scope.remove = (id) ->
			$scope.clear()

			$http.get '/feeds/trending/remove/' + id
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.success 'Successfully removed feed from trending feeds.'
						trending()
		
		$scope.add = ->
			$scope.name = ""
			$scope.clear()

			$http.get '/feeds/trending/add/' + $scope.feedId
				.success (data, status, headers, config) ->
					$scope.success 'Successfully added feed to trending feeds.'
					trending()

		trending()
		]