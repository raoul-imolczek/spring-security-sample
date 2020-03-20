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

.. image:: docs/img/roles-menu.png

Creation of users
+++++++++++++++++


Implementing Spring Security Resource server
--------------------------------------------

Beyond Roles and Scopes
-----------------------