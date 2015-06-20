define [ 'appModule' ], (app) ->
	app.controller 'FeedsController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.matches = 0
		$scope.feeds = []
		$scope.name = ""
		service.loggedin()
		
		search = ->
			if $scope.name.length > 0
				$http.post('/feeds/prefix', {search: $scope.name})
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.matches = data.data.feeds.length
							$scope.feeds = data.data.feeds
			else
				$scope.matches = 0
				$scope.feeds = []

		$scope.getInstagramUsername = (val) ->
			$http.jsonp 'https://api.instagram.com/v1/users/search',
				responseType: "json"
				params:
					callback: "JSON_CALLBACK"
					q: val
					count: "8"
					client_id: "e7ac00adad054cde8d2ee7ea6956dc8e"
			.then (response) ->
				response.data.data.map( (item) -> [item.username, item.profile_picture] )

		$scope.getTwitterUsername = (val) ->
			$http.get '/twitter/search',
				params:
					screenName: val
			.then (response) ->
				response.data.map( (item) -> [item.screen_name, item.profile_image_url] )

		$scope.getFacebookPageName = (val) ->
			$http.get '/facebook/search',
				params:
					pageName: val
			.then (response) ->
				response.data.data.map( (item) -> [item.name, item.picture.data.url] )
			
		$scope.change = ->
			if $scope.name.length >= 3
				search()
			else
				$scope.matches = 0
				$scope.feeds = []
				
		$scope.remove = ->
			$scope.error = false
			$scope.message = false

			$http.get('/feeds/remove/' + $scope.feedId)
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.form = false
						$scope.message = "Successfully deleted feed."
						search()
		
		$scope.create = ->
			$scope.feedDetails =
				"category": "player"
				"name": ""
				"facebookApi": ""
				"twitterApi": ""
				"instagramApi": ""
				"youtubeApi": ""
				"location": ""
			$scope.disabled = false
			$scope.error = false
			$scope.message = false
			$scope.form = 'create'

		$scope.cancel = -> $scope.form = false
		
		$scope.edit = (id) ->
			$scope.error = false
			$scope.message = false

			$http.get('/feeds/' + id)
				.success (data, status, headers, config) ->
					if data.result == 1
						$scope.feedId = id
						$scope.feedDetails =
							"category": data.data.category
							"name": data.data.name
							"facebookApi": data.data.facebookApi
							"twitterApi": data.data.twitterApi
							"instagramApi": data.data.instagramApi
							"youtubeApi": data.data.youtubeApi
							"location": data.data.location
						$scope.disabled = false
						$scope.form = 'edit'
		
		$scope.formSubmit = ->
			$scope.disabled = true
			$scope.error = false
			$scope.message = false
			
			if $scope.feedDetails['facebookApi'] == ""
				delete $scope.feedDetails['facebookApi']
				
			if $scope.feedDetails['twitterApi'] == ""
				delete $scope.feedDetails['twitterApi']
				
			if $scope.feedDetails['instagramApi'] == ""
				delete $scope.feedDetails['instagramApi']
				
			if $scope.feedDetails['youtubeApi'] == ""
				delete $scope.feedDetails['youtubeApi']
			
			if $scope.form == 'edit'
				$http.put('/feeds/' + $scope.feedId, $scope.feedDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.disabled = false
							$scope.message = "Successfully updated feed."
							search()
						else
							$scope.error = data.message
					.error (data, status, headers, config) ->
						$scope.error = "Oops! Problem editing feed. Please try again."
						$scope.disabled = false
			else
				$http.post( '/feeds/create', $scope.feedDetails )
					.success (data, status, headers, config) ->
						if data.result == 1
							$scope.form = false
							$scope.disabled = false
							$scope.message = "Successfully created new feed."
							search()
						else
							$scope.disabled = false
							$scope.error = data.message
					.error (data, status, headers, config) ->
						$scope.error = "Oops! Problem adding feed. Please try again."
						$scope.disabled = false

		$scope.getLocation = (val) ->
			$http.get 'http://maps.googleapis.com/maps/api/geocode/json',
				params:
					address: val
					sensor: false
			.then (response) ->
				response.data.results.map( (item) -> item.formatted_address )
		
		$scope.submit = search
		
		$scope.logout = service.logout
		]