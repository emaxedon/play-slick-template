define [ 'appModule' ], (app) ->

	app.controller 'AdminController', ['$scope', '$http', 'service', ($scope, $http, service) ->
		$scope.logout = service.logout

		$scope.clear = -> $scope.message = type: 'none', text: ''

		$scope.error = (s) -> $scope.message = type: 'error', text: s

		$scope.success = (s) -> $scope.message = type: 'success', text: s

		$scope.info = (s) -> $scope.message = type: 'info', text: s
		
		$scope.clear()
		]