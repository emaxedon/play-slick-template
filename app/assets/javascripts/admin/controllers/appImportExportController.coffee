define [ 'appModule' ], (app) ->

	app.controller 'ImportExportController', ['$scope', '$http', '$location', 'service', ($scope, $http, $location, service) ->
		$scope.error = false
		$scope.message = false
		$scope.info = false
		service.loggedin()

		$scope.fileChange = (elem) ->
			$scope.file = elem.files[0]
			$scope.$apply()

		$scope.importFeeds = ->
			$scope.info = "Importing feeds..."
			$scope.message = false
			$scope.error = false

			formData = new FormData()
			formData.append('file', $scope.file)

			$http.post '/feeds/upload', formData,
				transformRequest: angular.identity
				headers: {'Content-Type': undefined} #'multipart/form-data'
			.success (data, status, headers, config) ->
				$scope.message = "Successfully imported feeds."
				$scope.info = false
			.error (data, status, headers, config) ->
				$scope.error = data.message
				$scope.info = false

		]