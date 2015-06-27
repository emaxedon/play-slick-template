define [ 'appModule' ], (app) ->

	app.controller 'PopularController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.feeds = []
		$scope.error = false
		$scope.message = false
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
			$scope.error = false
			$scope.message = false

			$http.get '/feeds/popular/remove/' + id
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.message = "Successfully removed feed from popular feeds."
						popular()
		
		$scope.add = ->
			$scope.name = ""
			$scope.error = false
			$scope.message = false

			$http.get '/feeds/popular/add/' + $scope.feedId
				.success (data, status, headers, config) ->
					$scope.message = "Successfully added feed to popular feeds."
					popular()

		popular()
		]