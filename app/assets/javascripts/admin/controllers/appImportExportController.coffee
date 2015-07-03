define [ 'appModule' ], (app) ->

	app.controller 'ImportExportController', ['$scope', '$http', 'service', ($scope, $http, service) ->
		$scope.clear()
		service.loggedin()

		$scope.fileChange = (elem) ->
			$scope.file = elem.files[0]
			$scope.$apply()

		$scope.importFeeds = ->
			$scope.clear()
			$scope.info 'Importing feeds...'

			formData = new FormData()
			formData.append('file', $scope.file)

			$http.post '/feeds/upload', formData,
				transformRequest: angular.identity
				headers: {'Content-Type': undefined}
			.success (data, status, headers, config) ->
				$scope.success 'Successfully imported feeds.'
			.error (data, status, headers, config) ->
				$scope.error 'Oops! Failed to import feeds.'

		$scope.importUsers = ->
			$scope.clear()
			$scope.info 'Importing users...'

			formData = new FormData()
			formData.append('file', $scope.file)

			$http.post '/auth/upload', formData,
				transformRequest: angular.identity
				headers: {'Content-Type': undefined}
			.success (data, status, headers, config) ->
				$scope.success 'Successfully imported users.'
			.error (data, status, headers, config) ->
				$scope.error 'Oops! Failed to import users.'

		$scope.exportFeeds = -> window.open('/feeds/download')

		$scope.exportUsers = -> window.open('/auth/download')
			
		]