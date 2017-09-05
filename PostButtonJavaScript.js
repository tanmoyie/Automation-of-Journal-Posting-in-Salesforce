/* This JavaScript is a code written for performing necessary functions when user clicks the "Post"
button in Journal Detail page. When Post is clieked, a Transaction record is being created along with 
child objects (Transaction line item) from Journal & Journal Line Item
*/ 
{!REQUIRESCRIPT('/soap/ajax/29.0/connection.js')}

var buttonPost = document.getElementsByName("post"); /*post is button name; search & find from
inspect element in the browser*/
/* var checkbox = document.getElementByName('post').disabled = !this.checked;
window.onload=function()
{ document.getElementById('sendNewSms').disabled = true; }; */
buttonPost[0].value = 'Posted!'; /* Accountant MUST approve the Journal before it is being posted into
Transaction */
buttonPost[0].disabled = true;
/* Populate values for Transaction */
var transaction = new sforce.SObject("Transaction__c");
transaction.Journal__c = '{!Journal__c.Id}';

/*     ------ Need to fix bug in here */

/*
transaction.Company__c = '{!Journal__c.CompanyId__c}'; 
*/
/* Not company as it is a lookup field - use
MERGE FIELD (top of the dialog box) to get the field value */
/*transaction.Credit__c = '{!Journal__c.Credits__c}';
transaction.Currency__c = '{!Journal__c.Journal_Currency__c}';
**
/)
transaction.Date__c = new Date({!YEAR(Journal__c.Journal_Date__c)
}, {!MONTH(Journal__c.Journal_Date__c)
} - 1, {!DAY(Journal__c.Journal_Date__c)
});
transaction.Debit__c = '{!Journal__c.Debits__c}';
transaction.Description__c = '{!Journal__c.Journal_Description__c}';
transaction.Discard_Reason__c = '{!Journal__c.Discard_Reason__c}';
transaction.Invoice_Number__c = '{!Journal__c.Invoice_Number__c}';
transaction.Journal_Status__c = '{!Journal__c.Journal_Status__c}';
transaction.Period__c = '{!Journal__c.Period__c}';
transaction.Reference__c = '{!Journal__c.Reference__c}';
transaction.Total_Amount__c = '{!Journal__c.Total__c}';
transaction.Type__c = '{!Journal__c.Type__c}';
*/
var result = sforce.connection.create([transaction]);
if (result[0].getBoolean("success")) {
alert('Transaction Post Successful ');
} 
else {
alert('Could not create record ' + result);
}
/* Replicate Transaction Line Item by posting the journal */
/* Populate values for Transaction Line Item */
var transactionLineItem = new sforce.SObject("Transaction_Line_Item__c");
transactionLineItem.Line_Item__c = '{!Line_Item__c.Name}';
transactionLineItem.Account__c = '{!Line_Item__c.Account__c}';
/* transactionLineItem.Amount__c = '{!Line_Item__c.Amount__c}';
transactionLineItem.Credit__c = '{!Line_Item__c.Credit__c}';
transactionLineItem.Currency__c = '{!Line_Item__c.Currency__c}';
transactionLineItem.Debit__c = '{!Line_Item__c.Debit__c}';
transactionLineItem.Dimension__c = '{!Line_Item__c.Dimension_1__c}';
transactionLineItem.General_Ledger_Account__c = '{!Line_Item__c.General_Ledger_Account__c}';
transactionLineItem.Item__c = '{!Line_Item__c.Item__c}';
transactionLineItem.Line_Description__c = '{!Line_Item__c.Line_Description__c}';
transactionLineItem.Line_Type__c = '{!Line_Item__c.Line_Type__c}';
transactionLineItem.Reference__c = '{!Line_Item__c.Reference__c}';
transactionLineItem.Offset_Amount__c = '{!Line_Item__c.Offset_Amount__c}';
transactionLineItem.Tax_Amount__c = '{!Line_Item__c.Tax_Amount1_c__c}';
transactionLineItem.Tax_Code__c = '{!Line_Item__c.Tax_Code__c}';
transactionLineItem.Total_Amount__c = '{!Line_Item__c.Total_Amount__c}';
transactionLineItem.Transaction_Date__c = '{!Line_Item__c.Transaction_Date__c}'; */
var resultL = sforce.connection.create([transactionLineItem]);
if (result[0].getBoolean("success")) {
alert('Transaction line item Post Successful ' + result);
} else {
alert('Could not create record ' + result);
}
