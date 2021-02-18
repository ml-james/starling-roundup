### Starling Roundup API

#### General Info

* This application represents a fully fledged roundup microservice that can integrate with Starling bank's API, `https://developer.starlingbank.com/docs`.
* Please make a developer account with Starling using the instructions below.
* This application can be run from `StarlingRoundupApplication` main class.
* The application can be built using `./gradlew clean build`
* Application features include:
    * **Create a roundup goal**
      * Create a saving's account with Starling with a target for the amount specified.
      * Store state such as a maximum roundup amount each week and a roundup scale factor within this applications db store (H2).
    * **Retrieve a roundup goal (if one exists)**
    * **Edit a roundup goal**
      * Edit the savings target for the saving's account created by the create roundup goal.
      * Edit the maximum roundup amount or roundup scale factor used for subsequent weekly roundups.
    * **Perform a weekly roundup** 
      * Retrieve the last week's transactions from Starling's API and work out the total amount that is required to be rounded up.
      * Check there are sufficient funds in the main account in order to transfer over to the roundup goal savings account on week end.
      * Deposit money from the main account to the savings account.
      * Manage weekly roundup state for a particular account, where the state could be `TRANSFERRED`, `INSUFFICIENT_FUNDS` or `ZERO_REOUNDUP`.

#### Instructions to set up a Starling developer account

* Please create a Starling developer account at https://developer.starlingbank.com/. For this you will need to use an Authenticator app to set up 2FA.
 * Register an application with Starling bank.
 * Now create a sandbox environment and link it with this application.
 * Create a customer or a set of customers.
 * For a customer you will have an Account Holder UID, Access Token and Refresh Token. You will need this Access Token to authenticate yourself from this API to Starling's.
 * You can simulate account activity, e.g transactions manually or by simulating a whole series of activity with a click of the button.
 
 Please find the Postman collection to guide you when testing both this API and Starling's API.
 
#### Tech Stack

* `Gradle`
* `SpringBoot 2.2.7.RELEASE` 
* `Lombok 1.18.12`
* `Liquibase`  
* `H2 database`
* `MS SQL 7.4.1.jre8`
* `JUnit 5`  
* `okhttp3 mockwebserver`