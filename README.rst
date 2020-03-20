Spring security sample
======================

This sample provides guidelines to implement API security policies on a REST controller.
Spring security is used to implement an **OAuth 2.0 Resource server**.

This means that the REST controller is going to validate the **JWT Bearer token**
transmitted as a header alongside the API request.

Depending on the access control rules enforced for each method of the API controller,
the API client may have to obtain such token from an **OAuth 2.0 Authorization server**.

The sample REST controller of this repository illustrates a few examples of
access control rules that require:

- Either the **API Client** to have obtained some specific **scope**
- Or the authenticated **Resource owner** to have some specific **role**

Pre-requisites
--------------

In order to test this sample project, you need to setup an
**OAuth 2.0 Authorization Server**.

**Keycloak** is an easy to deploy / configure solution.

Get Keycloak: https://www.keycloak.org

You also have to understand the **OAuth 2.0 RFC**: https://tools.ietf.org/html/rfc6749
which defines the vocabulary used in this document and the **Authorization code grant**
flow that is illustrated in this sample.

Understanding Roles and Scopes
------------------------------

Let's take the example of an API meant to expose methods that allow an **API Client**
to perform requests on behalf of a **Resource owner**.

For each request, the **Resource server** must determine:

- Whether the API Client has the right to request the method, which can be
  *scope protected*. The **scope** describes the rights obtained by the client,
  they may have been subject to the resource owner's *consent*.
- Whether the Resource owner has the correct **role** to request the method.

A **scope** is dynamically granted by the Authorization server / Resource owner to
the API Client.

Inside of Keycloak for example, you can determine which scopes are associated to
a given API Client. For those scopes, you can decide whether they are:

- **Default scopes**: they will systematically be granted by the Authorization server.
- **Optional scopes**: they will be granted if requested by the client in the
  authorization request and may be subject to the consent of the Resource owner.

Whereas the **role** is inherent to the Resource Owner.  

Sample REST Controller
----------------------

This is a sample REST Controller before we setup API protection.

It consists of 3 methods:

- A `ping` method that any anonymous client may call.
- An `accounts` method for which the client should have the consent
  from an authenticated account holder to make requests.
- An `accountDetails` method that requires an additional scope.
  Moreover, there should be a verification whether the requested
  account's details is belonging to the authenticated user.

.. code-block:: java

	@RestController
	@RequestMapping("/sample/api/v1")
	public class SampleController {
	
		private static Logger logger = LoggerFactory.getLogger(SampleController.class);
	
		private AccountsService accountsService;
		
		public SampleController(AccountsService accountsService) {
			this.accountsService = accountsService;
		}
		
		/**
		 * This method has no authorization requirement
		 * An anonymous user may call this method
		 * @return pong
		 */
		@GetMapping("/ping")
		public String ping() {
			return "pong";
		}
		
		/**
		 * List accounts
		 * @return List of accounts
		 */
		@GetMapping("/accounts")
		public List<AccountResource> accounts(String subject) {
			List<AccountResource> accounts = accountsService.list(subject);
			return accounts;
		}
	
		/**
		 * Account details
		 * @param accountNumber The account number
		 * @return List of accounts
		 */
		@GetMapping("/accounts/{accountNumber}")
		public AccountResource accountDetails(@PathVariable String accountNumber) {
			return accountsService.details(accountNumber);
		}
		
	}

Keycloak configuration
----------------------

To start Keycloak, run `bin/standalone.bat`, Keycloak runs by default on port 8080.

Upon first connection, create the administrator account and login to the
administration console. 

Roles definition
++++++++++++++++

We're going to create a `customer` role.

That role is going to be necessary to make requests to the `accounts`
and `accountDetails` methods.

.. image:: docs/img/roles-menu.png

The customer role does not need to bear any particular attribute.

Just click *Add role* and name the role `customer`.

Creation of users
+++++++++++++++++

We're going to create two **users**:

- John Doe, who has the `customer` role
- Jane Doe, who *doesn't* have the `customer` role

.. image:: docs/img/users-menu.png

Click *Add user* to create a user and fill in the fields.

.. image:: docs/img/user-details.png

You also have to go under the *Credentials* tab to create a password for the user.
Do not forget to uncheck *Temporary* for the test, otherwise the user will be
asked to change his password upon first connection.

.. image:: docs/img/user-password.png

And finally, let's add the `customer` role to John Doe under the *Role Mappings*
tab:

.. image:: docs/img/user-roles.png

Definition of client scopes
+++++++++++++++++++++++++++

We're going to define two **client scopes**:

- `accounts:list`: a scope granted to an **API client** allowing it to request
  the `accounts` method.
- `accounts:details`: a scope granted to an **API client** allowing it to request
  the `accountDetails` method. 

.. image:: docs/img/scopes-menu.png

Click *Create* to create the Client scope and fill in the form:

.. image:: docs/img/scope.png

You can add text that will be displayed to the user if explicit consent should
be asked to him upon delivering the Authorization code.

Creation of the client
++++++++++++++++++++++

We're going to create `my_client`:

.. image:: docs/img/clients-menu.png

Make sure to check the *Confidential* mode and to activate *Authorization enabled*.

Besides, enabling *Consent required* will have the consequence of displaying a list
of requested consents to the user before an **Authorization code** is delivered
to the API Client.

.. image:: docs/img/client-1.png

An important feature of OAuth 2.0 is to rely on the redirection to a URL owned by
the API Client to deliver the Authorization code. So make sure that the URL
configured here matches the URL of the Spring boot microservice:

.. image:: docs/img/client-2.png

The confidential client must obtain credentials so as to exhcange the authorization
code for an access token. You can read it here:

.. image:: docs/img/client-credentials.png

Go to the *Client Scopes* tab in order to add the optional scopes to the client.
It means that the client must request those scopes in the Authorization request to
get them from Keycloak in the JWT Access token.

.. image:: docs/img/client-scopes.png

Finally you can implement a custom mapper to add a `roles` claim to the JWT
Access token.  

.. image:: docs/img/client-mappers.png

Use the built-in *groups* mapper:

.. image:: docs/img/mappers-builtin.png

And specify its name and in which tokens you want to display the claim:

.. image:: docs/img/client-roles-mapper.png

Implementing Spring Security Resource server
--------------------------------------------

Testing with Postman
--------------------

Beyond Roles and Scopes
-----------------------