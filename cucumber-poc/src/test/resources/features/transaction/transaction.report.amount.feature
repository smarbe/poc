Feature: Transaction report service test

  Background: 
  	  Given Transaction report service is available
      Given Transactions exists:
      |id		| currency			|amount	|date		|
  	  |1		| USD				|10.00	|12/09/2018	|
      |2		| EUR				|20.01	|13/09/2018	|
      |3		| EUR				|20.03	|13/09/2018	|

  Scenario: Create Transaction report for Amount
    When create report with date range: from "10/09/2018" to "15/09/2018" and count by field "Amount"
    Then expect report with following data:
    	|currency|count		|
    	|USD	 |10.0		|
    	|EUR	 |40.04		|
    Then expect total transaction count 3	