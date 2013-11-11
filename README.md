# PNC Account Balance Widget

This is a simple widget I'm working on for displaying my current bank account balance on the Android home screen.

It isn't feature complete, and you have to edit the sourecode to configure it. One day there may be a configuration menu. Also, I haven't coded for edge cases, so it is really easy to break.

## How It Works
It is actually pretty simple. PNC didn't have an API available for querying bank accounts (for reasons I can understand). They DID however have a text messaging service that I abused. It sends a text "BAL Chek" to PNC and then parses the response!

![Screenshotz](http://imgur.com/Xdz8bUj.jpg)
