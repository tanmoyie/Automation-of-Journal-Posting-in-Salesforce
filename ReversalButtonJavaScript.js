/* When user click the "Reverse" button in Transaction detail page of a particular Transaction,
this code will be invoked. What this JavaScript is doing is created a new Journal (reversed)  
This code execute PostWebService apex class to perform required operations
*/
{!REQUIRESCRIPT("/soap/ajax/15.0/connection.js")} 
{!REQUIRESCRIPT("/soap/ajax/15.0/apex.js")} 
if('{!Transaction__c.Reversal_Approval__c}' == true){ // If Reversal is not approve, through an alert "Need approval"
    if('{!Transaction__c.Reversed_Journal__c}' == ''){        
        var url = parent.location.href; 
        var returnFlag = sforce.apex.execute("PostWebService","methodPost",{RecordId:'{!Transaction__c.Id}',strType:'transaction'}); 
        if(returnFlag == ''){ 
            parent.location.href = url; 
            alert('Reversal Journal created successfully.');
        } 
        else { 
            alert(returnFlag); 
        }
    }
    else{
        alert('Reversal Journal is already created.');  // In case, this transaction is already reversed
    }
}
else{
    alert('Need Approval.');
}