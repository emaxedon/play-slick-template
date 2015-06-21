define [ 'appModule' ], (app) ->
	app.directive 'fileModel', ['$parse', ($parse) ->
		restrict: 'A',
		link: (scope, element, attrs) ->
			model = $parse(attrs.fileModel)
			modelSetter = model.assign
			
			element.bind 'change', ->
				scope.$apply ->
					modelSetter(scope, element[0].files[0])
	]

	app.service 'fileUpload', ['$http', ($http) ->
		this.uploadFileToUrl = (file, uploadUrl) ->
			fd = new FormData()
			fd.append('file', file)
			$http.post uploadUrl, fd,
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			.success (data, status, headers, config) -> null
			.error (data, status, headers, config) -> null
	]