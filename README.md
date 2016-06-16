### Auth0 Resource Server Sample - using Spring Boot and Spring Security

Demonstrates using Auth0 with Spring Boot and Spring Security to create a secure Resource Server - JWT Token Scope level Authorization

This sample would be suitable for headless APIs and SPA (single page application) backend end server scenarios.
It is specifically intended to demonstrate how to setup and read `scope` information from an Auth0 IDP JWT Access
Token, and use this information to control authentication and authorization to secured endpoints.

This sample application shows you how to:

 1. Configure and run Java based Spring API server with Auth0 and Spring Security
 2. 100% Java Configuration (Annotations)
 3. Secure one or more URL endpoints with Role / Authority based permissions (ROLE_USER, ROLE_ADMIN etc)
 4. Secure Java Services using method level security annotations for role based access control

Let's get started - it only takes a few minutes to have a working application with all the above.

This example relies upon `Spring Boot`.

Benefits of Spring Boot, in particular for traditional server-side web application / microservices architectures:

**Automatic configuration** - Spring Boot can automatically provide configuration for application functionality common to many Spring applications.

**Starter dependencies** - Tell Spring Boot what kind of functionality you need, and it will ensure that the libraries needed are added to the build.

**Command-line interface** - Optional feature of Spring Boot lets you write complete applications with just application code, but no need for a traditional
 project build.

**The Actuator** - Gives you insight into what's going on inside of a running Spring Boot application.

Useful quick start reference to getting started with [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-first-application.html)

### Prerequisites

In order to run this example you will need to have Maven installed. You can install Maven with [brew](http://brew.sh/):

```sh
brew install maven
```

Check that your maven version is 3.0.x or above:

```sh
mvn -v
```

### Setup

Create an [Auth0 Account](https://auth0.com) (if not already done so - free!).


#### From the Auth0 Dashboard


##### Create Client (Application)

Create an application - for the purposes of this sample - `app`

Add one or more `connections` to your application - for instance Google Social Connection,
or username-password DB connection.

##### Create Resource Server

Create a Resource Server - for the purposes of this sample - `ResourceServer`

In your Auth0 dashboard, go to `https://manage.auth0.com/#/apis`

Click the "Create API" button, and follow the screenshot below for required settings:


![](img/1.create_resourceserver.jpg)

Once created, select the new `ResourceServer`


Make a note of this information from the Settings Page:

![](img/2.resourceserver_settings.jpg)


Now assocate your `app` with the `ResourceServer` - go to the `Non Interactive Clients` tab and toggle the
`Authorized` switch

![](img/3.associate_app_to_resourceserver.jpg)


We will do the rest of the configuration from `Postman` - please ensure you have [POSTMAN](https://www.getpostman.com/)
installed - either Chrome App or Mac App.

Then from Postman import the collection found from root of this sample here: `postman/resource-server.postman_collection.json`

![](img/4.import_postman_collection.jpg)


If you view the descriptions for each of the defined endpoints imported, they have a description of what each
does.

![](img/5.import_result.jpg)


We patch the Resource Server with `https://{{AUTH0_DOMAIN}}/api/v2/resource-servers/{{RESOURCE_SERVER_ID}}`

passing in the following scope info:

`{"scopes":[{"value": "read:profiles"}, {"value": "read:profile"}, {"value": "write:profile"}, {"value": "delete:profile"}]}`

Now our resource server has read / write / delete profile scope information that we can assign none, some or all of to particular
clients.

For our application we patch the Resource Server with `https://{{AUTH0_DOMAIN}}/api/v2/client-grants/{{client_grant_id}}`

passing in the following scope info:

`{"scope": ["read:profiles", "read:profile", "write:profile", "delete:profile"]}`

Now our application has been granted credentials to all the scope info assigned to the Resource Server.

Suppose we had an untrusted application, then we might have only granted `read:profile` for example.

We send a `POST` request to `https://arcseldon.auth0.com/oauth/token`

passing in:

```
{
    "audience": "https://arcseldon.resourceserver.com",
    "grant_type": "client_credentials",
    "client_id": "eTQbNn3qxypLq2Lc1qQEThYL6R7M7MDh",
    "client_secret": "{{your secret}}"
 }
 ```

This gives us back an `access_token`:

Something like:

```
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FyY3NlbGRvbi5hdXRoMC5jb20vIiwic3ViIjoiZVRRYk5uM3F4eXBMcTJMYzFxUUVUaFlMNlI3TTdNRGhAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vYXJjc2VsZG9uLnJlc291cmNlc2VydmVyLmNvbSIsImV4cCI6MTQ2NjA5ODUzMCwiaWF0IjoxNDY2MDYyNTMwLCJzY29wZSI6InJlYWQ6cHJvZmlsZXMgcmVhZDpwcm9maWxlIHdyaXRlOnByb2ZpbGUgZGVsZXRlOnByb2ZpbGUifQ.CFSmn9k2F-E6q475j6egThv340RoYY1nJ8kh-_iNW7I",
  "token_type": "Bearer"
}
```

In Postman, logic has been added to automatically capture data that may be required on subsequent calls.

For instance, the `access_token` above will automatically be extracted from the result and stored in our
Manage Environments.

![](img/11.access_token_capture.jpg)


Inspecting this in JWT.io, we see:

![](img/8.jwt.io.jpg)


Notice, to make this call successfully you will have to use the Manage Environment options in POSTMAN and provide two
key pieces of information.

You should currently have something that looks like:

![](img/5.import_result.jpg)

And hopefully you can see on the top right, a drop down with option to `Manage Environment`

Screen shots below:

![](img/6.manage_env.jpg)

and

![](img/7.manage_env.jpg)


TO generate the `RESOURCE_SERVER_TOKEN` you shall need to go to the [Management API v2](https://auth0.com/docs/api/management/v2)

and add `create:resource_servers` and `create:client_grants` scopes. See screenshot:

![](img/9.management_api_token.jpg)

You can get the `RESOURCE_SERVER_ID` from the Resource Server -> Settings page of the Dashboard


### Inside the Application - update configuration information

Ok, this is a little different to when configuring a simple application (client) as we are configuring
for the Resource Server.

We need to update the information in `src/main/resources/auth0.properties`

Here is an example of a successfully completed `auth0.properties` file:

```
auth0.domain:arcseldon.auth0.com
auth0.issuer: https://arcseldon.auth0.com/
auth0.clientId: https://arcseldon.resourceserver.com
auth0.clientSecret: JCVcVWOXXXXXxWpKbFZKXXXXXpBEg2b
auth0.securedRoute:/api/v1/**
auth0.base64EncodedSecret: false
auth0.authorityStrategy: SCOPE
auth0.defaultAuth0ApiSecurityEnabled: false
```

`auth0.domain` - is your tenant domain as normal

`auth0.issuer` - is the full URL to your tenant - as above but replace `arcseldon` with your tenant name

`auth0.clientId` -  is the `IDENTIFIER` (not the `id`) of your resource server on the Resource Server Settings page

`auth0.clientSecret` -  is the `SIGNING SECRET` of your resource server on the Resource Server Settings page

`auth0.securedRoute` - is the secured endpoint - you can define more, or override / discard this value in the `AppConfig` (see later)

`auth0.base64EncodedSecret` - the Resource Server, unlike Auth0 Applications, does NOT base64 encode the Signing Secret. This should be `false`

`auth0.authorityStrategy` - our API library can support `roles`, `groups`, and `scope` depending on need. Here, we definitely want `SCOPE`


Note: There is a property in `auth0.properties` that you do not need to touch. Leave the value as `false`

`auth0.defaultAuth0ApiSecurityEnabled: false` - this ensures we do not autowire the default configuration file
provided with the `auth0-spring-security-api` library itself.


###### Clarification on the configuration settings defined for the endpoints

Here is our sample `AppConfig` entry where we specify the endpoints security settings.

```
  http.authorizeRequests()
                .antMatchers("/ping").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/profiles").hasAnyAuthority("read:profiles")
                .antMatchers(HttpMethod.GET, "/api/v1/profiles/**").hasAnyAuthority("read:profile", "read:profiles")
                .antMatchers(HttpMethod.POST, "/api/v1/profiles/**").hasAnyAuthority("write:profile")
                .antMatchers(HttpMethod.PUT, "/api/v1/profiles/**").hasAnyAuthority("write:profile")
                .antMatchers(HttpMethod.DELETE, "/api/v1/profiles/**").hasAnyAuthority("delete:profile")
                .antMatchers(securedRoute).authenticated();
```

Here, we assign `scope` settings to particular endpoints, for this CRUD example we are saying

read:profile / read:profiles - for GET http actions

write:profile -  for POST and PUT http actions

delete:profile -  for DELETE http actions


Note also, the sample adds an extra level of security at the `Services` level too:

![](img/10.service_layer_security.jpg)


### Build and Run

In order to build and run the project execute:

```sh
mvn spring-boot:run
```

### Test the API

You can use the predefined CRUD operations in POSTMAN to make calls to the Resource server now it is up and running..

Experiment by removing scopes and regenerating the `access_token` - for example remove `write:profile` and re-try a POST


Key Point: Remember to include the `Authorization: Bearer {{ACCESS_TOKEN}}"` header (already done in the Postman collection).

---

### Screenshot of Postman making API calls:


#### Postman Example

![](img/12.postman_run.jpg)

---


## What is Auth0?

Auth0 helps you to:

* Add authentication with [multiple authentication sources](https://docs.auth0.com/identityproviders), either social like **Google, Facebook, Microsoft Account, LinkedIn, GitHub, Twitter, Box, Salesforce, amont others**, or enterprise identity systems like **Windows Azure AD, Google Apps, Active Directory, ADFS or any SAML Identity Provider**.
* Add authentication through more traditional **[username/password databases](https://docs.auth0.com/mysql-connection-tutorial)**.
* Add support for **[linking different user accounts](https://docs.auth0.com/link-accounts)** with the same user.
* Support for generating signed [Json Web Tokens](https://docs.auth0.com/jwt) to call your APIs and **flow the user identity** securely.
* Analytics of how, when and where users are logging in.
* Pull data from other sources and add it to the user profile, through [JavaScript rules](https://docs.auth0.com/rules).

## Create a free account in Auth0

1. Go to [Auth0](https://auth0.com) and click Sign Up.
2. Use Google, GitHub or Microsoft Account to login.

## Issue Reporting

If you have found a bug or if you have a feature request, please report them at this repository issues section. Please do not report security vulnerabilities on the public GitHub issue tracker. The [Responsible Disclosure Program](https://auth0.com/whitehat) details the procedure for disclosing security issues.

## Author

[Auth0](auth0.com)

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE.txt) file for more info.
