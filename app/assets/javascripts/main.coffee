require.config
  baseUrl: '/assets'

  shim: 
    'bootstrap': deps: [ 'jquery' ]

  paths:
    'jquery': 'lib/jquery/jquery.min'
    'bootstrap': 'lib/bootstrap/js/bootstrap.min'

require [
  'jquery'
], ($) ->
  $(document).ready ->
    $('#form-reset').on 'submit', ->
      password = $('#form-reset #inputPassword').val()
      confirmPassword = $('#form-reset #inputConfirmPassword').val()

      if (password == confirmPassword)
        $.ajax
          url: '/auth/password'
          type: 'PUT'
          contentType: 'application/json'
          dataType: 'json'
          data: JSON.stringify({ password: password })
          success: (data) ->
            $('.alert:not(.hide)').addClass('hide')
            $('.alert-success')
              .removeClass('hide')
              .append('Success! You have modified your password.')
            $('#form-reset').hide()
      else
        $('.alert:not(.hide)').addClass('hide')
        $('.alert-danger')
          .removeClass('hide')
          .append('Passwords do not match.')

      return false