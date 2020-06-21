## Starling roundup technical challenge

What follows is a summary of any assumptions that were made in developing this mini application and some justifications on certain implementation details. I will try to keep this as brief (and cheerful) as possible.

#### Assumptions

- Interpretation of the task: since it was stated to NOT build an OAuth implementation, the task was essentially to design and build an application, against the API specification that demonstrates something that could work.
- A user will be able to hit the "roundup creation" endpoint from their account.
- Some other cron job with executive permissions (Starling super user??) will then be able to hit the "roundup action" endpoint to actually perform the required roundups, for a list of accounts.
- We cannot build a naive implementation - there must implicitly be built in protection against a customer trying to create two roundup goals against one account AND / OR the cron hitting roundup action "misfiring".

#### Design

A note on packages:

- common - includes common functionalities that are not specific to a particular endpoint necessarily. For example, fund confirmation package has a simple contract of returning a boolean, this is contained in its own package ready to be used by any feature if necessary.

- feature - includes the features that I decided to implement, which were a "creation" endpoint and an "action" endpoint.

I'm not sure if this organisation is particularly, but it something that I am used to and I quite like.

### Create roundup goal

- Hit the endpoint with all the information required to create a savings goal essentially. We also have path variables here in the request. Since the customer will be creating a roundup account from within their account, it is reasonable to assume we have information such as accountUid, defaultCategoryUid and the currency of the account. Here I assumed that we only want to roundup transactions related to the default category (i.e. "main" part of a person's account). Also the currency we need when making the create savings goal.
- First lets check to see if we have already saved a "roundup account" against that account uid. This hits an internal repository that essentially manages roundup accounts and their mappings to savings goal uids. If we have not already saved an account lets continue.
- Now lets go and create a savings goal.
- Now lets go and save the roundup account meta data into the repository owned by this application.
- Lets return to the user the savings goal uid that we have just created.

### Roundup action

- Here the information required by this super user cron is just a list of accountuid..theses could easily be retrieved from the roundup account repository or just taken from another list etc.
- First lets check to see if we have created a round up savings goal.
- Now retrieve the round up value
    - If we go into transaction service here we calculate the window for which we're going to retrieve transactions - if we check round up state service to see if the last successful state's "week end" is equal to the current runs. If so...throw an exception!
    - If we however have no calculated round up for the current week end then lets go ahead retrieve transactions.
    - Then we calculate the roundup. I asked the user for a roundup multiplier as well as a roundup maximum (the counter intuitive thing about roundup is that the more you spend the more you save...exactly when you can afford to save less so i thought a cap would be a good feature to implement)
    - Now lets return the roundup amount as well as the string representation of last weeks end.
- Lets ask fund confirmation service to check if the account against which roundup was created can afford the transfer.
    - If so lets deposit the funds, with a randomly created string (didn't think too much about this)
    - Lets update the state with identifying features - transferUid, week end, state and the roundupUid, which is just an autoincremented identifier in the RoundupAccount table)
    - Return to caller
    
    - If not sufficient funds, lets save that there was a success but with this state and lets not try and roundup as even if they have some money deposited we do not really want to try again - further more trying again and again and again will keep failing continuosly which is no good for anyone.
    - Now lets return to caller

- Exceptions are handled and appropriate status codes returned.

As a final point of note, this version of the application has not been fully tested, due to time constraints. I chose to prioritise testing a few classes that were integral in the logic. I plan to complete testing as a further exercise!
