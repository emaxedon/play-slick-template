define [ 'appModule' ], (app) ->

	app.controller 'PopularController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.feeds = []
		$scope.clear()
		service.loggedin()

		popular = ->
			$http.get '/feeds/popular'
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

			$http.get '/feeds/popular/remove/' + id
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.success 'Successfully removed feed from popular feeds.'
						popular()
		
		$scope.add = ->
			$scope.name = ""
			$scope.clear()

			$http.get '/feeds/popular/add/' + $scope.feedId
				.success (data, status, headers, config) ->
					$scope.success 'Successfully added feed to popular feeds.'
					popular()

		popular()
		]