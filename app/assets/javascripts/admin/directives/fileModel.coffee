define [ 'appModule' ], (app) ->
	app.directive 'fileModel', ['$parse', 'fileUpload', ($parse, fileUpload) ->
		restrict: 'A',
		link: (scope, element, attrs) ->
			model = $parse(attrs.fileModel)
			modelSetter = model.assign
			
			element.bind 'change', ->
				scope.$apply ->
					modelSetter(scope, element[0].files[0])
					
					# use file upload service
					file = element[0].files[0]
					fileUpload file, 'feeds/upload'
	]