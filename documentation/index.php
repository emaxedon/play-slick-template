<!doctype html>
<html>
<head>
	<title>URLs</title>
	<link rel="stylesheet" href="css/style.css">

	<style>
	.sidebar {
		background: #fafafa;
		display: block;
		position: fixed;
		top: 0;
		left: 0;
		bottom: 0;
		height: 100%;
		overflow: scroll;
	}
	
	.sidebar > div {
		
	}

	.sidebar ul {
		margin-bottom: 40px;
	}
	
	.sidebar ul li {
		margin-bottom: 10px;
	}

	.sidebar ul li a {
		color: rgb(51,51,51);
	}

	.content section {
		border-top: 20px solid #fff;
	}

	.content section h4 {
		margin-bottom: 30px;
		line-height: 30px;
	}
	</style>
</head>
<body>
	<p>&nbsp;</p>
	<div class="container">
	<div class="row">
		<!-- SIDEBAR -->
		<div class="col-xs-4 sidebar">
		<div>
			<h1>TOC <span class="label label-default">v1.1.2</span></h1>
			<hr>
	
			<h4>Login</h4>

			<ul>
				<li><span class="label label-success">POST</span> <a href="#post-login">/login</a></li>
			</ul>

			<h4>Logout</h4>

			<ul>
				<li><span class="label label-primary">GET</span> <a href="#get-logout">/logout</a></li>
			</ul>

			<h4>Forgot Password</h4>

			<ul>
				<li><span class="label label-success">POST</span> <a href="#post-forgotpassword">/forgotpassword</a></li>
			</ul>

			<h4>Questions</h4>

			<ul>
				<li><span class="label label-primary">GET</span> <a href="#get-questionsinterests">/questions/interests</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-questionsmotivations">/questions/motivations</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-questionsexperiences">/questions/experiences</a></li>
			</ul>

			<h4>Registration</h4>

			<ul>
				<li><span class="label label-success">POST</span> <a href="#post-registration">/registration</a></li>
			</ul>

			<h4>User</h4>

			<ul>
				<li><span class="label label-primary">GET</span> <a href="#get-user">/user</a></li>
				<li><span class="label label-info">PUT</span> <a href="#put-user">/user</a></li>
				<li><span class="label label-success">POST</span> <a href="#post-userdelete">/user/delete</a></li>
				<li><span class="label label-success">POST</span> <a href="#post-userchangepassword">/user/changepassword</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-usertrips">/user/trips</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-usertripsid">/user/trips/{trip_id}</a></li>
				<li><span class="label label-warning">*NEW</span> <span class="label label-primary">GET</span> <a href="#get-userrecommendeddestinations">/user/recommended/destinations</a></li>
			</ul>

			<h4>Destinations</h4>

			<ul>
				<li><span class="label label-primary">GET</span> <a href="#get-destinations">/destinations</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-destinationsid">/destinations/{destination_id}</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-destinationssearch">/destinations/?s={searchstring | minchar: 3}</a></li>
			</ul>

			<h4>Local Experiences</h4>

			<ul>
				<!-- <li><span class="label label-primary">GET</span> <a href="#get-localexperiences">/destinations/{destination_id}/localexperiences</a></li> -->
				<li><span class="label label-primary">GET</span> <a href="#get-localexperiencesparam">/destinations/{destination_id}/localexperiences/<br/>?datefrom={YYYY-MM-DD}<br/>&amp;dateto={YYYY-MM-DD}<br/>&amp;travelers={integer}<br/>&amp;pricefrom={float}<br/>&amp;priceto={float}<br/>&amp;duration={float}<br/>&amp;type={integer}<br/>&amp;p={integer}</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-localexperiencesidparam">/destinations/{destination_id}/localexperiences/<br/>{experience_id}<br/>?datefrom={YYYY-MM-DD}<br/>&amp;dateto={YYYY-MM-DD}<br/>&amp;travelers={integer}</a></li>
			</ul>

			<h4>Local Accommodations</h4>

			<ul>
				<!-- <li><span class="label label-primary">GET</span> <a href="#get-localaccommodations">/destinations/{destination_id}/localaccommodations</a></li> -->
				<li><span class="label label-primary">GET</span> <a href="#get-localaccommodationsparam">/destinations/{destination_id}/localaccommodations/<br/>?datefrom={YYYY-MM-DD}<br/>&amp;dateto={YYYY-MM-DD}<br/>&amp;travelers={integer}<br/>&amp;pricefrom={float}<br/>&amp;priceto={float}<br/>&amp;type={integer}<br/>&amp;amenity={integer}<br/>&amp;p={integer}</a></li>
				<li><span class="label label-primary">GET</span> <a href="#get-localaccommodationsidparam">/destinations/{destination_id}/localaccommodations/<br/>{accommodation_id}<br/>?datefrom={YYYY-MM-DD}<br/>&amp;dateto={YYYY-MM-DD}<br/>&amp;travelers={integer}</a></li>
			</ul>

			<h4>Bookings</h4>

			<ul>
				<li><span class="label label-success">POST</span> <a href="#post-bookings">/bookings</a></li>
			</ul>
		</div>
		</div>
		<!-- end SIDEBAR -->

		<!-- CONTENT -->
		<div class="col-xs-8 col-xs-offset-4 content">
			<!-- section -->
			<section id="post-login">
			<h4><span class="label label-success">POST</span> /login</h4>

			<p><strong>Payload Examples</strong></p>
			<div class="highlight">
<pre>
{
	username: "test@email.com",
	password: "1111111"
}
</pre>
			</div>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span> <small>Returns Token</small></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: "cdde4451b0a9ec1a5380f8a066e4f23e16485575"
}
</pre>
			</div>
	
			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Invalid username and/or password."
}
</pre>
			</div>
			</section>
			<!-- end section -->

			<!-- section -->
			<section id="get-logout">
			<h4><span class="label label-primary">GET</span> /logout</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: null,
	message: "Successfully logged out"
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="post-forgotpassword">
			<h4><span class="label label-success">POST</span> /forgotpassword</h4>
			
			<p><strong>Payload Examples</strong></p>
			<div class="highlight">
<pre>
{
	email: "test@email.com"
}
</pre>
			</div>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span> <small>Return success for every email, including ones that don't exist.</small></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: null,
	message: "Please check your email for password reset instructions."
}
</pre>
			</div>
	
			<p><span class="label label-danger">ERROR</span> <small>Invalid only if not an email.</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Invalid email address."
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-questionsinterests">
			<h4><span class="label label-primary">GET</span> /questions/interests</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: [
		{
			id: 1,
			name: "Adventure",
			image: "filename.jpg"
		},
		{
			id: 2,
			name: "Architecture, art, and design",
			image: "filename.jpg"
		},
		{
			id: 3,
			name: "Culture",
			image: "filename.jpg"
		},
		...
	]
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-questionsmotivations">
			<h4><span class="label label-primary">GET</span> /questions/motivations</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: [
		{
			id: 1,
			name: "Relax and get away"
		},
		{
			id: 2,
			name: "Enrich your knowledge"
		},
		{
			id: 3,
			name: "Test your limits"
		},
		...
	]
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-questionsexperiences">
			<h4><span class="label label-primary">GET</span> /questions/experiences</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: [
		[
			{
				id: 1,
				name: "Experience title 1",
				image: "filename.jpg"
			},
			{
				id: 2,
				name: "Experience title 2",
				image: "filename.jpg"
			}
		],
		[
			{
				id: 3,
				name: "Experience title 3",
				image: "filename.jpg"
			},
			{
				id: 4,
				name: "Experience title 4",
				image: "filename.jpg"
			}
		],
		...
	]
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="post-registration">
			<h4><span class="label label-success">POST</span> /registration</h4>
			
			<p><strong>Payload Examples</strong></p>
			<div class="highlight">
<pre>
{
	username: "test@email.com",
	password: "11111111",
	name: "Charles Xavier"
}
</pre>
			</div>

			<div class="highlight">
<pre>
{
	username: "test@email.com",
	password: "11111111",
	name: "Charles Xavier",
	interests: [1,2,3],
	motivations: [1,2,3],
	experiences: [1,2,3]
}
</pre>
			</div>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: "cdde4451b0a9ec1a5380f8a066e4f23e16485575",
	message: "Successfully registered."
}
</pre>
<!-- <pre>
{
	result: 1,
	data: null,
	message: "Please check your email to complete your registration."
}
</pre> -->
			</div>
	
			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "[Email]|[Password]|[Name] missing."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Invalid email address."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Password is too short. Please use a minimum of 8 characters."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "An account already exists with this email address."
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-user">
			<h4><span class="label label-primary">GET</span> /user</h4>
		
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		name: "Manuel Pineault",
		email: "email@something.com"
	}
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Invalid Or Missing Token.</small></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>


			</section>
			<!-- end section -->

			<!-- section -->
			<section id="put-user">
			<h4><span class="label label-info">PUT</span> /user</h4>
			
			<p><strong>Payload Examples</strong></p>
			<div class="highlight">
<pre>
{
	name: "New Name"
}
</pre>
			</div>

			<div class="highlight">
<pre>
{
	email: "newemail@something.com"
}
</pre>
			</div>

			<div class="highlight">
<pre>
{
	name: "New Name",
	email: "newemail@something.com"
}
</pre>
			</div>

			<p><strong>Results</strong></p>
			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		name: "Manuel Pineault",
		email: "email@something.com"
	}
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Email address is already taken."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Invalid email address."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Invalid Or Missing Token.</small></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>
			</section>
			<!-- end section -->

			<!-- section -->
			<section id="post-userdelete">
			<h4><span class="label label-success">POST</span> /user/delete</h4>

			<p><strong>Payload Examples</strong></p>
			<div class="highlight">
<pre>
{
	password: "12345678"
}
</pre>
			</div>

			<p><strong>Results</strong></p>
			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: null,
	message: "Success"
}
</pre>
			</div>
			
			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Please submit password to fully delete the account."
}
</pre>
			</div>
			
			<p><span class="label label-danger">ERROR</span> <small>Invalid Or Missing Token.</small></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>
			</section>
			<!-- end section -->

			<!-- section -->
			<section id="post-userchangepassword">
			<h4><span class="label label-success">POST</span> /user/changepassword</h4>

			<p><strong>Payload Examples</strong></p>
			<div class="highlight">
<pre>
{
	password: "12345678"
}
</pre>
			</div>

			<p><strong>Results</strong></p>
			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: null,
	message: "Success"
}
</pre>
			</div>
			
			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Password is too short. Please use a minimum of 8 characters."
}
</pre>
			</div>
			
			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Password is too long. Please use a maximum of 16 characters."
}
</pre>
			</div>
			
			<p><span class="label label-danger">ERROR</span> <small>Invalid Or Missing Token.</small></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>

			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-usertrips">
			<h4><span class="label label-primary">GET</span> /user/trips</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: []
}
</pre>
			</div>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: [
		{
			id: 1,
			booking_id: "AA345453XS",
			date: "Thu, 30 Apr 2015 23:59:59 GMT",
			status: "pending"
			sub_total: 1567.50,
			taxes: 255.50,
			total: 1823.00,
			localexperiences: [
				{
					date: "2015-10-23",
					quantity: 1,
					localexperience: {
						...
					}
				},
				{
					date: "2015-10-24",
					quantity: 2,
					localexperience: {
						...
					}
				}
			],
			localaccommodations: [
				{
					date: "2015-10-23",
					nights: 1,
					localaccommodation: {
						...
						room: {
							id: 1,
							name: "Deluxe Suite",
							bed: "King",
							description: "We put a nice chocolate on your bed",
							price_per_night: 79.00,
							price_per_additional_guest: 39.00,
							max_guests: 2,
							max_additional_guests: 1,
							room_amenities: [
								{
									id: 1,
									name: "Free Wifi"
								},
								{
									id: 2,
									name: "In-suite Bath"
								}
							],
							images: [
								"filename1.jpg",
								"filename2.jpg"
							]
						}
					}
				},
				{
					date: "2015-10-24",
					nights: 3,
					localaccommodation: {
						...
						room: {
							id: 2,
							name: "Small Suite",
							bed: "Double",
							description: "We put a nice chocolate on your bed",
							price_per_night: 69.00,
							price_per_additional_guest: 39.00,
							max_guests: 2,
							max_additional_guests: 1,
							room_amenities: [
								{
									id: 1,
									name: "Free Wifi"
								},
								{
									id: 2,
									name: "In-suite Bath"
								}
							],
							images: [
								"filename1.jpg",
								"filename2.jpg"
							]
						}
					}
				}
			]
		},
		{
			id: 2,
			booking_id: "EE345453XS",
			date: "Thu, 30 Apr 2015 23:59:59 GMT",
			status: "pending"
			sub_total: 1567.50,
			taxes: 255.50,
			total: 1823.00,
			localexperiences: [
				{
					date: "2015-10-23",
					quantity: 1,
					localexperience: {
						...
					}
				},
				{
					date: "2015-10-24",
					quantity: 2,
					localexperience: {
						...
					}
				}
			],
			localaccommodations: [
				{
					date: "2015-10-23",
					nights: 1,
					localaccommodation: {
						...
						room: {
							id: 1,
							name: "Deluxe Suite",
							bed: "King",
							description: "We put a nice chocolate on your bed",
							price_per_night: 79.00,
							price_per_additional_guest: 39.00,
							max_guests: 2,
							max_additional_guests: 1,
							room_amenities: [
								{
									id: 1,
									name: "Free Wifi"
								},
								{
									id: 2,
									name: "In-suite Bath"
								}
							],
							images: [
								"filename1.jpg",
								"filename2.jpg"
							]
						}
					}
				},
				{
					date: "2015-10-24",
					nights: 3,
					localaccommodation: {
						...
						room: {
							id: 2,
							name: "Small Suite",
							bed: "Double",
							description: "We put a nice chocolate on your bed",
							price_per_night: 69.00,
							price_per_additional_guest: 39.00,
							max_guests: 2,
							max_additional_guests: 1,
							room_amenities: [
								{
									id: 1,
									name: "Free Wifi"
								},
								{
									id: 2,
									name: "In-suite Bath"
								}
							],
							images: [
								"filename1.jpg",
								"filename2.jpg"
							]
						}
					}
				}
			]
		}
	]

}
</pre>
			</div>


			<p><span class="label label-danger">ERROR</span> <small>Invalid Or Missing Token.</small></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>
			</section>
			<!-- end section -->

			<!-- section -->
			<section id="get-usertripsid">
			<h4><span class="label label-primary">GET</span> /user/trips/{trip_id}</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		id: 2,
		booking_id: "EE345453XS",
		date: "Thu, 30 Apr 2015 23:59:59 GMT",
		status: "pending"
		sub_total: 1567.50,
		taxes: 255.50,
		total: 1823.00,
		localexperiences: [
			{
				date: "2015-10-23",
				quantity: 1,
				localexperience: {
					...
				}
			},
			{
				date: "2015-10-24",
				quantity: 2,
				localexperience: {
					...
				}
			}
		],
		localaccommodations: [
			{
				date: "2015-10-23",
				nights: 1,
				localaccommodation: {
					...
					room: {
						id: 1,
						name: "Deluxe Suite",
						bed: "King",
						description: "We put a nice chocolate on your bed",
						price_per_night: 79.00,
						price_per_additional_guest: 39.00,
						max_guests: 2,
						max_additional_guests: 1,
						room_amenities: [
							{
								id: 1,
								name: "Free Wifi"
							},
							{
								id: 2,
								name: "In-suite Bath"
							}
						],
						images: [
							"filename1.jpg",
							"filename2.jpg"
						]
					}
				}
			},
			{
				date: "2015-10-24",
				nights: 3,
				localaccommodation: {
					...
					room: {
						id: 2,
						name: "Small Suite",
						bed: "Double",
						description: "We put a nice chocolate on your bed",
						price_per_night: 69.00,
						price_per_additional_guest: 39.00,
						max_guests: 2,
						max_additional_guests: 1,
						room_amenities: [
							{
								id: 1,
								name: "Free Wifi"
							},
							{
								id: 2,
								name: "In-suite Bath"
							}
						],
						images: [
							"filename1.jpg",
							"filename2.jpg"
						]
					}
				}
			}
		]
	}
}
</pre>
			</div>
	
			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Trip not found."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Invalid Or Missing Token.</small></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>


			</section>
			<!-- end section -->
			
			<!-- section -->
			<section id="get-userrecommendeddestinations">
			<h4><span class="label label-primary">GET</span> /user/recommended/destinations</h4>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: [
			{
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru",
			description: "Lorem ipsum dolor sed",
			images: [
				"lima-1.jpg",
				"lima-2.jpg"
			]
		},
		{
			id: 2,
			name: "Dubai",
			region: null,
			country: "United Arab Emirates",
			description: "Lorem ipsum dolor sed",
			images: [
				"dubai-3.jpg"
			]
		},
		{
			id: 3,
			name: "Vancouver",
			region: "British Columbia",
			country: "Canada",
			description: "Lorem ipsum dolor sed",
			images: [
				"vancouver-4.jpg"
			]
		},
			{
			id: 4,
			name: "Montreal",
			region: "Quebec",
			country: "Canada",
			description: "Lorem ipsum dolor sed",
			images: [
				"montreal-5.jpg"
			]
		}
	]
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>

			</section>
			<!-- end section -->

			<!-- section -->
			<section id="get-destinations">
			<h4><span class="label label-primary">GET</span> /destinations</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: [
		{
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru",
			description: "Lorem ipsum dolor",
			images: [
				"filename1.jpg",
				"filename2.jpg"
			]
		},
		{
			id: 2,
			name: "Dubai",
			region: null,
			country: "United Arab Emirates",
			description: "Lorem ipsum dolor",
			images: [
				"filename1.jpg",
				"filename2.jpg"
			]
		},
		...
	]
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-destinationsid">
			<h4><span class="label label-primary">GET</span> /destinations/{destination_id}</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		id: 1,
		name: "Lima",
		region: "Lima",
		country: "Peru",
		description: "Lorem ipsum dolor",
		images: [
			"filename1.jpg",
			"filename2.jpg"
		]
	}
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Destination not found."
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-destinationssearch">
			<h4><span class="label label-primary">GET</span> /destinations/?s={searchstring | minchar: 3}</h4>
			
			<p><strong>Parameter Examples</strong></p>
			
			<div class="highlight">
<pre>
/destinations/?s=
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/?s=lim
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/?s=spaced%20string
</pre>
			</div>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: []
}
</pre>
			</div>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: [
		{
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru",
			description: "Lorem ipsum dolor",
			images: [
				"filename1.jpg",
				"filename2.jpg"
			]
		},
		{
			id: 2,
			name: "Dubai",
			region: null,
			country: "United Arab Emirates",
			description: "Lorem ipsum dolor",
			images: [
				"filename1.jpg",
				"filename2.jpg"
			]
		},
		...
	]
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
<!-- 			<section id="get-localexperiences">
			<h4><span class="label label-primary">GET</span> /destinations/{destination_id}/localexperiences</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: null,
				to: null
			},
			duration: null,
			type: null
		},
		filteroptions: {
			prices: {
				from: null,
				to: null
			},
			durations: [],
			types: []
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localexperiences: [],
		pages: {
			results: {
				from: 0,
				to: 0,
				total: 0
			},
			current: 1,
			total: 0
		}
	}
}
</pre>
			</div>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: null,
				to: null
			},
			duration: null,
			type: null
		},
		filteroptions: {
			prices: {
				from: 39,
				to: 75
			},
			durations: [
				0.5,
				2
			],
			types: [
				{
					id: 1,
					name: "Private Tour"
				},
				{
					id: 2,
					name: "Group Tour"
				},
				{
					id: 3,
					name: "Another Tour Type"
				},
				{
					id: 3,
					name: "Another Tour Type"
				}
			]
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localexperiences: [
			{
				id: 1,
				destination_id: 1,
				vendor_id: 1,
				name: "Paragliding",
				price_perperson: 39,
				duration: 0.5,
				min_people: 1,
				max_people: null,
				cancellation_time: null,
				cancelation_refund: 0.0,
				booking_time: 24,
				address_line1: "1234 name street",
				address_line2: null,
				city: "Lima",
				region: null,
				country: "Peru",
				postalzip: "15074",
				long: -12.126101,
				lat: -77.0346315,
				types: [
					{
						id: 1,
						name: "Private Tour"
					},
					{
						id: 2,
						name: "Group Tour"
					},
					{
						id: 3,
						name: "Another Tour Type"
					}
					],
					images: [
						"lima-1.jpg"
					],
					description: "Lorem ipsum",
					included: "Dolor sed",
					restrictions: "Ipsum lorem",
					staff_recommendation: null,
					availability: [
						1,
						3,
						7
					]
			},
			{
				id: 2,
				destination_id: 1,
				vendor_id: 2,
				name: "Desert Sport Racing",
				price_perperson: 75,
				duration: 2,
				min_people: 2,
				max_people: 6,
				cancellation_time: 72,
				cancelation_refund: 50,
				booking_time: 12,
				address_line1: "3456 name street",
				address_line2: "unit 101",
				city: "Lima",
				region: null,
				country: "Peru",
				postalzip: "13074",
				long: -12.118443,
				lat: -77.026482,
				types: [
					{
						id: 3,
						name: "Another Tour Type"
					}
				],
				images: [
					"lima-2.jpg"
				],
				description: "Lorem ipsum",
				included: "Dolor sed",
				restrictions: "Ipsum lorem",
				staff_recommendation: "This is an awesome place!",
				availability: [
						2,
						5
				]
			}
		],
		pages: {
			results: {
				from: 0,
				to: 9,
				total: 1000
			},
			current: 1,
			total: 112
		}
	}
}
</pre>
			</div>
			</section> -->
			<!-- end section -->


			<!-- section -->
			<section id="get-localexperiencesparam">
			<h4><span class="label label-primary">GET</span> /destinations/{destination_id}/localexperiences/?datefrom={YYYY-MM-DD}&amp;dateto={YYYY-MM-DD}&amp;travelers={integer}&amp;pricefrom={float}&amp;priceto={float}&amp;duration={float}&amp;type={integer}</h4>
			
			<p><strong>Parameter Examples</strong></p>


			<div class="highlight">
<pre>
/destinations/1/localexperiences/
</pre>
			</div>
			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28&amp;dateto=2015-03-01
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=0.00
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00&amp;duration=2.0
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00&amp;duration=2.0&amp;type=3
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00&amp;duration=2.0&amp;type=3&amp;p=1
</pre>
			</div>
		
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: null,
				to: null
			},
			duration: null,
			type: null
		},
		filteroptions: {
			prices: {
				from: null,
				to: null
			},
			durations: [],
			types: []
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localexperiences: [],
		pages: {
			results: {
				from: 0,
				to: 0,
				total: 0
			},
			current: 1,
			total: 0
		}
	}
}
</pre>
			</div>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: 50.00,
				to: 1000.00
			},
			duration: 2.0,
			type: 5
		},
		filteroptions: {
			prices: {
				from: 39,
				to: 75
			},
			durations: [
				0.5,
				2
			],
			types: [
				{
					id: 1,
					name: "Private Tour"
				},
				{
					id: 2,
					name: "Group Tour"
				},
				{
					id: 3,
					name: "Another Tour Type"
				},
				{
					id: 3,
					name: "Another Tour Type"
				}
			]
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localexperiences: [
			{
				id: 1,
				destination_id: 1,
				vendor_id: 1,
				name: "Paragliding",
				price_perperson: 39,
				duration: 0.5,
				min_people: 1,
				max_people: null,
				cancellation_time: null,
				cancelation_refund: 0.0,
				booking_time: 24,
				address_line1: "1234 name street",
				address_line2: null,
				city: "Lima",
				region: null,
				country: "Peru",
				postalzip: "15074",
				long: -12.126101,
				lat: -77.0346315,
				types: [
					{
						id: 1,
						name: "Private Tour"
					},
					{
						id: 2,
						name: "Group Tour"
					},
					{
						id: 3,
						name: "Another Tour Type"
					}
					],
					images: [
						"lima-1.jpg"
					],
					description: "Lorem ipsum",
					included: "Dolor sed",
					restrictions: "Ipsum lorem",
					staff_recommendation: null,
					availability: [
						1,
						3,
						7
					]
			},
			{
				id: 2,
				destination_id: 1,
				vendor_id: 2,
				name: "Desert Sport Racing",
				price_perperson: 75,
				duration: 2,
				min_people: 2,
				max_people: 6,
				cancellation_time: 72,
				cancelation_refund: 50,
				booking_time: 12,
				address_line1: "3456 name street",
				address_line2: "unit 101",
				city: "Lima",
				region: null,
				country: "Peru",
				postalzip: "13074",
				long: -12.118443,
				lat: -77.026482,
				types: [
					{
						id: 3,
						name: "Another Tour Type"
					}
				],
				images: [
					"lima-2.jpg"
				],
				description: "Lorem ipsum",
				included: "Dolor sed",
				restrictions: "Ipsum lorem",
				staff_recommendation: "This is an awesome place!",
				availability: [
						2,
						5
				]
			}
		],
		pages: {
			results: {
				from: 0,
				to: 9,
				total: 1000
			},
			current: 1,
			total: 112
		}
	}
}
</pre>
			</div>

			</section>
			<!-- end section -->

			<!-- section -->
			<section id="get-localexperiencesidparam">
			<h4><span class="label label-primary">GET</span> /destinations/{destination_id}/localexperiences/{experience_id}?datefrom={YYYY-MM-DD}&amp;dateto={YYYY-MM-DD}&amp;travelers={integer}</h4>
			
			<p><strong>Parameter Examples</strong></p>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/2
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/2?datefrom=2015-02-28
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/2?datefrom=2015-02-28&amp;dateto=2015-03-01
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localexperiences/2?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2
</pre>
			</div>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: null,
			dates: {
				from: null,
				to: null
			}
			travelers: null
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localexperience: {
			id: 2,
			destination_id: 1,
			vendor_id: 2,
			name: "Desert Sport Racing",
			price_perperson: 75,
			duration: 2,
			min_people: 2,
			max_people: 6,
			cancellation_time: 72,
			cancelation_refund: 50,
			booking_time: 12,
			address_line1: "3456 name street",
			address_line2: "unit 101",
			city: "Lima",
			region: null,
			country: "Peru",
			postalzip: "13074",
			long: -12.118443,
			lat: -77.026482,
			types: [
				{
					id: 3,
					name: "Another Tour Type"
				}
			],
			images: [
				"lima-2.jpg"
			],
			description: "Lorem ipsum",
			included: "Dolor sed",
			restrictions: "Ipsum lorem",
			staff_recommendation: "This is an awesome place!",
			availability: [
					2,
					5
			]
		}
	}
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Local experience not found."
}
</pre>
			</div>
			</section>
			<!-- end section -->



			<!-- section -->
			<!-- <section id="get-localaccommodations">
			<h4><span class="label label-primary">GET</span> /destinations/{destination_id}/localaccommodations</h4>
			
			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: null,
				to: null
			},
			type: null,
			amenity: null
		},
		filteroptions: {
			prices: {
				from: 39,
				to: 75
			},
			durations: [
				0.5,
				2
			],
			types: [
				{
					id: 1,
					name: "Private Tour"
				},
				{
					id: 2,
					name: "Group Tour"
				},
				{
					id: 3,
					name: "Another Tour Type"
				},
				{
					id: 3,
					name: "Another Tour Type"
				}
			]
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localaccommodations: [],
		pages: {
			results: {
				from: 0,
				to: 0,
				total: 0
			},
			current: 1,
			total: 0
		}
	}
}
</pre>
			</div>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: 50.0,
				to: 1000.00
			},
			type: 2,
			amenity: 3
		},
		filteroptions: {
			prices: {
				from: 39,
				to: 75
			},
			types: [
				{
					id: 1,
					name: "Hotel
				},
				{
					id: 2,
					name: "Hostel"
				},
				{
					id: 3,
					name: "Bed and Breakfast"
				}
			],
			amenities: [
				{
					id: 1,
					name: "Pool"
				},
				{
					id: 2,
					name: "Gym"
				},
				{
					id: 3,
					name: "Office Room"
				}
			]
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localaccommodations: [
			{
				id: 1,
				destination_id: 1,
				vendor_id: 3,
				name: "Montreal Hotel",
				description: "Lorem ipsum dolor",
				checkin_time: 15.5,
				checkout_time: 12.0,
				types: [
					{
						id: 1,
						name: "Hotel
					},
					{
						id: 3,
						name: "Bed and Breakfast"
					}
				],
				amenities: [
					{
						id: 2,
						name: "Gym"
					},
					{
						id: 3,
						name: "Office Room"
					}
				],
				images: [
					"filename1.jpg",
					"filename2.jpg"
				],
				rooms: [
					{
						id: 1,
						name: "Deluxe Suite",
						bed: "King",
						description: "Chocolate on your bed",
						price_per_night: 79.50,
						price_per_additional_guest: 15.75,
						max_guests: 2,
						max_additional_guests: 1,
						room_amenities: [
							{
								id: 1,
								name: "Free Wifi"
							},
							{
								id: 2,
								name: "In-suite Bath"
							}
						],
						images: [
							"filename1.jpg",
							"filename2.jpg"
						]
					},
					{
						id: 2,
						name: "Small Suite",
						bed: "Double",
						description: "Chocolate on your bed",
						price_per_night: 69.50,
						price_per_additional_guest: 0.00,
						max_guests: 2,
						max_additional_guests: 0,
						room_amenities: [
							{
								id: 1,
								name: "Free Wifi"
							}
						],
						images: [
							"filename1.jpg",
							"filename2.jpg"
						]
					}
				]
			}
		],
		pages: {
			results: {
				from: 1,
				to: 9,
				total: 80
			},
			current: 1,
			total: 9
		}
	}
}
</pre>
			</div>
			</section> -->
			<!-- end section -->


			<!-- section -->
			<section id="get-localaccommodationsparam">
			<h4><span class="label label-primary">GET</span> /destinations/{destination_id}/localaccommodations/?datefrom={YYYY-MM-DD}&amp;dateto={YYYY-MM-DD}&amp;travelers={integer}&amp;pricefrom={float}&amp;priceto={float}&amp;type={integer}&amp;amenity={integer}</h4>
			
			<p><strong>Parameter Examples</strong></p>


						<div class="highlight">
<pre>
/destinations/1/localaccommodations/
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28&amp;dateto=2015-03-01
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=0.00
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00&amp;type=3
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00&amp;type=3&amp;amenity=2
</pre>
						</div>

						<div class="highlight">
<pre>
/destinations/1/localaccommodations/?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2&amp;pricefrom=50.00&amp;priceto=1000.00&amp;type=3&amp;amenity=2&amp;p=1
</pre>
						</div>


			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: 50.0,
				to: 1000.00
			},
			type: 2,
			amenity: 3
		},
		filteroptions: {
			prices: {
				from: 39.0,
				to: 75.0
			},
			types: [
				{
					id: 1,
					name: "Hotel
				},
				{
					id: 2,
					name: "Hostel"
				},
				{
					id: 3,
					name: "Bed and Breakfast"
				}
			],
			amenities: [
				{
					id: 1,
					name: "Pool"
				},
				{
					id: 2,
					name: "Gym"
				},
				{
					id: 3,
					name: "Office Room"
				}
			]
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localaccommodations: [],
		pages: {
			results: {
				from: 0,
				to: 0,
				total: 0
			},
			current: 1,
			total: 0
		}
	}
}
</pre>
			</div>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: 1,
			dates: {
				from: "2015-12-23",
				to: "2015-12-29"
			}
			travelers: 2
		},
		filters: {
			price: {
				from: 50.0,
				to: 1000.00
			},
			type: 2,
			amenity: 3
		},
		filteroptions: {
			prices: {
				from: 39.0,
				to: 75.0
			},
			types: [
				{
					id: 1,
					name: "Hotel
				},
				{
					id: 2,
					name: "Hostel"
				},
				{
					id: 3,
					name: "Bed and Breakfast"
				}
			],
			amenities: [
				{
					id: 1,
					name: "Pool"
				},
				{
					id: 2,
					name: "Gym"
				},
				{
					id: 3,
					name: "Office Room"
				}
			]
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localaccommodations: [
			{
				id: 1,
				destination_id: 1,
				vendor_id: 3,
				name: "Montreal Hotel",
				description: "Lorem ipsum dolor",
				checkin_time: 15.5,
				checkout_time: 12.0,
				types: [
					{
						id: 1,
						name: "Hotel
					},
					{
						id: 3,
						name: "Bed and Breakfast"
					}
				],
				amenities: [
					{
						id: 2,
						name: "Gym"
					},
					{
						id: 3,
						name: "Office Room"
					}
				],
				images: [
					"filename1.jpg",
					"filename2.jpg"
				],
				rooms: [
					{
						id: 1,
						name: "Deluxe Suite",
						bed: "King",
						description: "Chocolate on your bed",
						price_per_night: 79.50,
						price_per_additional_guest: 15.75,
						max_guests: 2,
						max_additional_guests: 1,
						room_amenities: [
							{
								id: 1,
								name: "Free Wifi"
							},
							{
								id: 2,
								name: "In-suite Bath"
							}
						],
						images: [
							"filename1.jpg",
							"filename2.jpg"
						]
					},
					{
						id: 2,
						name: "Small Suite",
						bed: "Double",
						description: "Chocolate on your bed",
						price_per_night: 69.50,
						price_per_additional_guest: 0.00,
						max_guests: 2,
						max_additional_guests: 0,
						room_amenities: [
							{
								id: 1,
								name: "Free Wifi"
							}
						],
						images: [
							"filename1.jpg",
							"filename2.jpg"
						]
					}
				]
			}
		],
		pages: {
			results: {
				from: 1,
				to: 9,
				total: 80
			},
			current: 1,
			total: 9
		}
	}
}
</pre>
			</div>
			</section>
			<!-- end section -->


			<!-- section -->
			<section id="get-localaccommodationsidparam">
			<h4><span class="label label-primary">GET</span> /destinations/{destination_id}/localaccommodations/{accommodation_id}?datefrom={YYYY-MM-DD}&amp;dateto={YYYY-MM-DD}&amp;travelers={integer}</h4>
			
			<p><strong>Parameter Examples</strong></p>

			<div class="highlight">
<pre>
/destinations/1/localaccommodations/2
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localaccommodations/2?datefrom=2015-02-28
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localaccommodations/2?datefrom=2015-02-28&amp;dateto=2015-03-01
</pre>
			</div>

			<div class="highlight">
<pre>
/destinations/1/localaccommodations/2?datefrom=2015-02-28&amp;dateto=2015-03-01&amp;travelers=2
</pre>
			</div>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: {
		search: {
			destination: null,
			dates: {
				from: null,
				to: null
			}
			travelers: null
		},
		destination: {
			id: 1,
			name: "Lima",
			region: "Lima",
			country: "Peru"
		},
		localaccommodation: {
			id: 1,
			destination_id: 1,
			vendor_id: 3,
			name: "Montreal Hotel",
			description: "Lorem ipsum dolor",
			checkin_time: 15.5,
			checkout_time: 12.0,
			types: [
				{
					id: 1,
					name: "Hotel
				},
				{
					id: 3,
					name: "Bed and Breakfast"
				}
			],
			amenities: [
				{
					id: 2,
					name: "Gym"
				},
				{
					id: 3,
					name: "Office Room"
				}
			],
			images: [
				"filename1.jpg",
				"filename2.jpg"
			],
			rooms: [
				{
					id: 1,
					name: "Deluxe Suite",
					bed: "King",
					description: "Chocolate on your bed",
					price_per_night: 79.50,
					price_per_additional_guest: 15.75,
					max_guests: 2,
					max_additional_guests: 1,
					room_amenities: [
						{
							id: 1,
							name: "Free Wifi"
						},
						{
							id: 2,
							name: "In-suite Bath"
						}
					],
					images: [
						"filename1.jpg",
						"filename2.jpg"
					]
				},
				{
					id: 2,
					name: "Small Suite",
					bed: "Double",
					description: "Chocolate on your bed",
					price_per_night: 69.50,
					price_per_additional_guest: 0.00,
					max_guests: 2,
					max_additional_guests: 0,
					room_amenities: [
						{
							id: 1,
							name: "Free Wifi"
						}
					],
					images: [
						"filename1.jpg",
						"filename2.jpg"
					]
				}
			]
		}
	}
}
</pre>
			</div>
	
			<p><span class="label label-danger">ERROR</span></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	message: "Local accommodation not found."
}
</pre>
			</div>

			</section>
			<!-- end section -->


			<!-- section -->
			<section id="post-bookings">
			<h4><span class="label label-success">POST</span> /bookings</h4>
			
			<p><strong>Payload Examples</strong></p>
			<div class="highlight">
<pre>
{
	username: "something@email.com",
	billing: {
		contact: {
			first_name: "Jeanne"
			last_name: "Grey",
			email: "different@email.com",
			phone: 5145556677
		},
		address: {
			address_line1: "1234 Something Street",
			address_line2: null,
			city: "Montreal",
			region: "Quebec",
			country: "Canada",
			postalzip: "A1B2C3"
		},
		card: {
			name: "Jeanne Grey",
			number: 5555666677778888.
			expiry: 0117,
			cvc: 123
		}
	},
	localexperiences: [
		{
			id: 1,
			date: "2015-10-23",
			quantity: 1
		},
		{
			id: 3,
			date: "2015-10-24",
			quantity: 2
		}
	],
	localaccomodations: [
		{
			id: 1,
			date: "2015-10-23",
			nights: 1,
			room_id: 2
		},
		{
			id: 2,
			date: "2015-10-24",
			nights: 3,
			room_id: 5
		}
	],
	sub_total: 1545.50,
	taxes: 245.00,
	total: 1790.50
}
</pre>
			</div>

			<p><strong>Results</strong></p>

			<p><span class="label label-success">SUCCESS</span> <small>Returns Trip ID</small></p>
			<div class="highlight">
<pre>
{
	result: 1,
	data: 4
}
</pre>
			</div>
	
			<p><span class="label label-danger">ERROR</span> <small>Error Code 1: Missing Information</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	error: 1,
	message: "Username is missing."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Error Code 1: Missing Information</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	error: 1,
	message: "Billing information is incomplete."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Error Code 1: Missing Information</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	error: 1,
	message: "[Local experiences]|[Local accommodations] missing."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Error Code 1: Missing Information</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	error: 1,
	message: "Subtotal is missing."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Error Code 1: Missing Information</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	error: 1,
	message: "Total is missing."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Error Code 2: Return array with object needing to be updated.</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: {
		localexperiences: [
			...
		],
		localaccommodations: [
			...
		]
	},
	error: 2,
	message: "The price has changed for one or more items."
}
</pre>
			</div>

			<p><span class="label label-danger">ERROR</span> <small>Error Code 3: One or more venues is no longer available.</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: {
		localexperiences: [
			...
		],
		localaccommodations: [
			...
		]
	},
	error: 3,
	message: "The dates and/or rooms for one or more local accomodations and/or experiences are no longer available."
}
</pre>
			</div>


			<p><span class="label label-danger">ERROR</span> <small>Error Code 4: Invalid credit card credentials.</small></p>
			<div class="highlight">
<pre>
{
	result: 0,
	data: null,
	error: 4,
	message: "Invalid credit card credentials."
}
</pre>
			</div>


			<p><span class="label label-danger">ERROR</span> <small>Invalid Or Missing Token.</small></p>
			<div class="highlight">
<pre>
403 Forbidden
</pre>
			</div>


			</section>
			<!-- end section -->


		</div>
		<!-- end CONTENT -->
	</div>	
	</div>
	



</body>
</html>