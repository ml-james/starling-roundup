## Starling roundup technical challenge

What follows is a summary of any assumptions that were made in developing this application and some justifications on certain implementation details. I will try to keep this as brief (and cheerful) as possible.

#### Assumptions

- Since it was stated to NOT build an OAuth implementation, my interpretation of the task was essentially to design and build an application, against the API specification, that demonstrates something that **could** work.
- A bank account owner would be able to hit a "roundup creation" endpoint from their account, where they can input savings goal specific information.
- Some other cron job with executive permissions (a Starling super user??) would be able to hit a "roundup action" endpoint to actually perform the required roundups, once a goal is set-up, for a list of accounts.
- The application should not be naive and highly mistrustful - there should therefore be built-in protection against a customer trying to create two roundup goals against one account AND / OR the cron hitting the roundup action endpoint "misfiring".

#### Design

A note on packages:

- common - includes common functionalities that are not specific to a particular endpoint necessarily. For example, fund confirmation package has a simple contract of returning a boolean depending on whether an account can afford a particular transfer; this is contained in its own package ready to be used by any future feature if necessary.

- feature - includes the features that I decided to implement, which were a "creation" endpoint and an "action" endpoint.

I'm not sure if this organisation is particularly common, but it something that I am used to and I quite like.

### Create roundup goal

##### Step 1
Hit the endpoint with all the information required to create a savings goal. We also have path variables here in the request. Since the customer will be creating a roundup account from within their account, it is reasonable to assume we have information such as accountUid, defaultCategoryUid and the currency of the account. Here I assumed that we only want to roundup transactions related to the default category (i.e. "main" part of a person's account). Also the currency we need when creating the savings goal.

##### Step 2

Lets check to see if we have already saved a "roundup account" against that account uid. This hits an internal repository that essentially manages roundup accounts and their mappings to savings goal uids. If we have not already saved an account lets continue, otherwise, lets throw a client error.

##### Step 3

Now lets go and create a savings goal.

##### Step 4

Now lets go and save the roundup account meta-data into the repository owned by this application.

##### Step 5

Let's return to the user the savings goal uid that we have just created.

### Roundup action

##### Step 1

Here the information required by this super user cron is just a list of account uids; these could easily be retrieved from the roundup account repository or just taken from another list etc. Let's hit the endpoint.

##### Step 2

Let's check to see if we have created a round up savings goal against this account uid already, if so let's throw an exception.

##### Step 3

Now let's retrieve the round up value for the previous weeks transactions. If we go into transaction service here we calculate the window for which we're going to retrieve transactions. We want to double check we're not rounding up transactions we've already considered here so let's check roundup state service. If the last successful roundup state's "week end" is equal to the current runs then throw an exception!

##### Step 4

If we have not calculated a roundup for the current week end against this account then lets go ahead and retrieve the transactions.

##### Step 5

When creating the roundup savings goal in another endpoint the user was asked for a roundup multiplier as well as a roundup maximum (the counter-intuitive thing about a roundup feature is that the more you spend the more you save...exactly when you can afford to save **less** so I thought a cap would be a good feature to implement). We should therefore roundup to these specifications.

##### Step 6

Now let's return the roundup amount as well as the string representation of last weeks end.

##### Step 7

Now lets ask fund confirmation service to check if the account against which the roundup goal was created can afford the transfer. If so let's deposit the funds, with a randomly created transferUid string (I didn't think too much about this).

##### Step 8
 
Let's update the state with the required identifying features - transferUid, week end, state and the roundupUid (the auto incremented value created when saving roundupAccount information and a foreign key in the roundup_state table).

##### Step 9

If insufficient funds then lets mark as completed, but mark the state as insufficient funds; we wouldn't want to keep retrying this over and over. Either way return to caller the state.

### Final Note

As a final point of note, this version of the application has not been fully tested, due to time constraints. I chose to prioritise testing a few classes that I thought were integral in the logic. I plan to complete testing as a further exercise!
