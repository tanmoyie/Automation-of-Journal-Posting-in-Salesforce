// POST & REVERSAL button related apex class
global class PostWebService {
    webservice static String methodPost(String RecordId , string strType){
        String strmsg = '';
        try{
            if(strType == 'journal')
            {
                Journal__c objJour = [SELECT Id, Approved__c, Company__c, Credits__c, Debits__c, Discard_Reason__c, 
                                      Invoice_Number__c, Journal_Currency__c ,Journal_Date__c, Journal_Description__c,
                                      Journal_Status__c, Reference__c, Total__c, Type__c, Period__c, General_Ledger_Account1__c, 
                                      (SELECT Id, Name, Account__c, Amount__c, Credit__c, Currency__c, Debit__c, 
                                       Dimension_1__c, General_Ledger_Account__c, Item__c, Journal__c,
                                       Line_Description__c, Line_Type__c, Offset_Amount__c, Reference__c, Total_Amount_Receivable__c,
                                       Tax_Amount1_c__c, Tax_Code__c, Total_Amount__c, Transaction_Date__c, Tax_code__r.Tax_rate__c, 
                                       Tax_Code__r.Tax_GLA__c, Account__r.Account_Payable__c, General_Ledger_Account_Offset__c, 
                                       Journal__r.Bank_Account__r.General_Ledger_Account__c
                                       FROM Line_Items__r) 
                                      from Journal__c WHERE id=: RecordId]; 
                // Transaction to be posted for Journal based on Line input  --- Sales or Payable
                if (objJour.Type__c == 'Sales' || objJour.Type__c == 'Payable'){
                    Transaction__c objTran = new Transaction__c();
                    objTran.Journal__c = objJour.Id;
                    objTran.Company__c = objJour.Company__c;
                    objTran.Currency__c = objJour.Journal_Currency__c;
                    objTran.Date__c = objJour.Journal_Date__c;
                    objTran.Description__c = objJour.Journal_Description__c;
                    objTran.Discard_Reason__c = objJour.Discard_Reason__c;
                    objTran.Invoice_Number__c = objJour.Invoice_Number__c;
                    objTran.Journal_Status__c = 'Completed';
                    objTran.Period__c = objJour.Period__c;
                    objTran.Reference__c = objJour.Reference__c;
                    objTran.Type__c = objJour.Type__c;
                    insert objTran;
                    // Update 2 journal fields
                    objJour.Transaction__c = objTran.Id ; 
                    objJour.Journal_Status__c = 'Completed';
                    update objJour;
                    
                    List<Transaction_Line_Item__c> lstTLI = new  List<Transaction_Line_Item__c>(); 
                    for (Line_Item__c objL:objJour.Line_Items__r){
                        Transaction_Line_Item__c TLI = new Transaction_Line_Item__c();
                        TLI.Transaction__c = objTran.id;
                        TLI.Line_Item__c = objL.Id; 
                        TLI.Amount__c = objL.Amount__c; 
                        TLI.Currency__c  = objL.Currency__c; 
                        TLI.Credit__c = objL.Credit__c; 
                        TLI.Dimension__c  = objL.Dimension_1__c; 
                        TLI.General_Ledger_Account__c = objL.General_Ledger_Account__c; 
                        TLI.Line_Description__c = objL.Line_Description__c; 
                        TLI.Line_Type__c  = objL.Line_Type__c; 
                        TLI.Reference__c = objL.Reference__c; 
                        TLI.Tax_Amount__c  = objL.Tax_Amount1_c__c; 
                        TLI.Total_Amount__c = objL.Total_Amount__c; 
                        TLI.Transaction_Date__c  = objL.Transaction_Date__c; 
                        lstTLI.add(TLI);
                        // for Account Mehtod 3
                        Transaction_Line_Item__c TLI2 = new Transaction_Line_Item__c();
                        TLI2.Transaction__c = objTran.id;
                        TLI2.Line_Item__c = objL.Id; 
                        TLI2.Account__c = objL.Account__c; 
                        TLI2.Amount__c = objL.Total_Amount_Receivable__c; // receivable
                        TLI2.General_Ledger_Account__c = objL.Account__r.Account_Payable__c; // Account to track Receivable
                        TLI2.Line_Description__c = objL.Line_Description__c; 
                        TLI2.Total_Amount__c = objL.Total_Amount_Receivable__c; 
                        lstTLI.add(TLI2);
                        // for Tax Method 3
                        Transaction_Line_Item__c TLI3 = new Transaction_Line_Item__c();
                        TLI3.Transaction__c = objTran.id;
                        TLI3.Line_Item__c = objL.Id; 
                        TLI3.Amount__c = objL.Tax_Amount1_c__c; // tax line
                        TLI3.General_Ledger_Account__c = objL.Tax_Code__r.Tax_GLA__c; //Tax_GLA__c
                        TLI3.Line_Description__c = objL.Line_Description__c; 
                        TLI3.Total_Amount__c = objL.Tax_Amount1_c__c; // as this is for TAX
                        lstTLI.add(TLI3);
                    }
                    if(lstTLI.size() > 0)  { 
                        insert lstTLI;    
                    }
                }
                
                // ----------------- Bank journal 
                else if (objJour.Type__c == 'Bank'){
                    Transaction__c objTran = new Transaction__c();
                    objTran.Journal__c = objJour.Id;
                    objTran.Company__c = objJour.Company__c;
                    objTran.Currency__c = objJour.Journal_Currency__c;
                    objTran.Date__c = objJour.Journal_Date__c;
                    objTran.Description__c = objJour.Journal_Description__c;
                    objTran.Discard_Reason__c = objJour.Discard_Reason__c;
                    objTran.Invoice_Number__c = objJour.Invoice_Number__c;
                    objTran.Journal_Status__c = 'Completed';
                    objTran.Period__c = objJour.Period__c;
                    objTran.Reference__c = objJour.Reference__c;
                    objTran.Type__c = objJour.Type__c;
                    insert objTran;
                    // Update 2 journal fields
                    objJour.Transaction__c = objTran.Id ; 
                    objJour.Journal_Status__c = 'Completed';
                    update objJour;
                    
                    List<Transaction_Line_Item__c> lstTLI = new  List<Transaction_Line_Item__c>(); 
                    for (Line_Item__c objL:objJour.Line_Items__r){

                      // GLA 
                        Transaction_Line_Item__c TLI = new Transaction_Line_Item__c();
                        TLI.Transaction__c = objTran.id;
                        TLI.Line_Item__c = objL.Id; 
                        TLI.Account__c = objL.Account__c; 
                        TLI.Amount__c = objL.Amount__c; // 
                        TLI.Currency__c  = objL.Currency__c; 
                        TLI.Credit__c = objL.Credit__c; 
                        TLI.Dimension__c  = objL.Dimension_1__c; 
                        TLI.General_Ledger_Account__c = objL.Journal__r.Bank_Account__r.General_Ledger_Account__c; 
                        TLI.Line_Description__c = objL.Line_Description__c; 
                        TLI.Line_Type__c  = objL.Line_Type__c; 
                        TLI.Reference__c = objL.Reference__c; 
                  //      TLI.Tax_Amount__c  = objL.Tax_Amount1_c__c; 
                        TLI.Total_Amount__c = objL.Total_Amount__c; 
                        TLI.Transaction_Date__c  = objL.Transaction_Date__c; 
                        lstTLI.add(TLI);
                        
                      // Offset GLA Mehtod 3
                        Transaction_Line_Item__c TLI2 = new Transaction_Line_Item__c();
                        TLI2.Transaction__c = objTran.id;
                        TLI2.Line_Item__c = objL.Id; 
                        TLI2.Account__c = objL.Account__c; 
                        TLI2.Amount__c = objL.Offset_Amount__c; // receivable
                  //      TLI2.General_Ledger_Account__c = objL.Account__r.Account_Receivable__c; // Account to track Receivable
                   //     TLI2.Offset_GLA__c = objL.General_Ledger_Account_Offset__c; 
                        TLI2.General_Ledger_Account__c = objL.General_Ledger_Account_Offset__c; 
                        TLI2.Line_Description__c = objL.Line_Description__c; 
                        TLI2.Total_Amount__c = objL.Total_Amount_Receivable__c; 
                        lstTLI.add(TLI2);
                        
                    }
                    if(lstTLI.size() > 0)  { 
                        insert lstTLI;    
                    }
                }
             // ----------- Manual Journal 
                else{
                    Transaction__c objTran = new Transaction__c();
                    objTran.Journal__c = objJour.Id;
                    objTran.Company__c = objJour.Company__c;
                    objTran.Currency__c = objJour.Journal_Currency__c;
                    objTran.Date__c = objJour.Journal_Date__c;
                    objTran.Description__c = objJour.Journal_Description__c;
                    objTran.Discard_Reason__c = objJour.Discard_Reason__c;
                    objTran.Invoice_Number__c = objJour.Invoice_Number__c;
                    objTran.Journal_Status__c = 'Completed';
                    objTran.Period__c = objJour.Period__c;
                    objTran.Reference__c = objJour.Reference__c;
                    objTran.Type__c = objJour.Type__c;
                    insert objTran;
                    // Update 2 journal fields
                    objJour.Transaction__c = objTran.Id ; 
                    objJour.Journal_Status__c = 'Completed';
                    update objJour;
                    
                    List<Transaction_Line_Item__c> lstTLI = new  List<Transaction_Line_Item__c>(); 
                    for (Line_Item__c objL:objJour.Line_Items__r){
                        Transaction_Line_Item__c TLI = new Transaction_Line_Item__c();
                        TLI.Transaction__c = objTran.id;
                        TLI.Line_Item__c = objL.Id; 
                        TLI.Account__c = objL.Account__c; 
                        TLI.Amount__c = objL.Amount__c; 
                        TLI.Currency__c  = objL.Currency__c; 
                        TLI.Credit__c = objL.Credit__c; 
                        TLI.Dimension__c  = objL.Dimension_1__c; 
                        TLI.General_Ledger_Account__c = objL.General_Ledger_Account__c; 
                        TLI.Line_Description__c = objL.Line_Description__c; 
                        TLI.Line_Type__c  = objL.Line_Type__c; 
                        TLI.Reference__c = objL.Reference__c; 
                        TLI.Tax_Code__c = objL.Tax_Code__c; 
                        TLI.Tax_Amount__c  = objL.Tax_Amount1_c__c; 
                        TLI.Total_Amount__c = objL.Total_Amount__c; 
                        TLI.Transaction_Date__c  = objL.Transaction_Date__c; 
                        lstTLI.add(TLI);
                    }
                    if(lstTLI.size() > 0)  { 
                        insert lstTLI;    
                    }
                }
            }
            else if(strType == 'transaction')
            {
                //Put all the logic here similar to journal
                Transaction__c objTran = [SELECT Id, Company__c, Discard_Reason__c, Currency__c, Description__c,
                                          Invoice_Number__c, Date__c, Line_Item__c, 
                                          Period__c, Transaction__c, Type__c, Reference__c,
                                          (SELECT Id, Name, Account__c, Amount__c, Credit__c, Currency__c, Debit__c, 
                                           Dimension__c, General_Ledger_Account__c, Item__c, 
                                           Line_Description__c, Line_Type__c, Reference__c, 
                                           Tax_Amount__c, Tax_Code__c, Total_Amount__c, Transaction_Date__c, Tax_code__r.Tax_rate__c, 
                                           Tax_Code__r.Tax_GLA__c, Account__r.Account_Receivable__c
                                           FROM Transaction_Line_Items__r) 
                                          from Transaction__c WHERE id=: RecordId]; 
                // Create Reversed Journal
                Journal__c journal = new Journal__c();
                journal.Transaction_Reversal_source__c = objTran .Id;
                journal.Company__c = objTran.Company__c;
                journal.Journal_Description__c = 'Reversed Journal: ' + objTran.Description__c; // if Comment needed for clarification
                journal.Invoice_Number__c = objTran.Invoice_Number__c;
                journal.Reference__c = 'Reversed Journal:  ' + objTran.Reference__c;
                journal.Journal_Currency__c = objTran.Currency__c;
                DateTime dt = objTran.Date__c;
                journal.Journal_Date__c = date.newinstance(dT.year(), dT.month(), dT.day());
                journal.Discard_Reason__c = objTran.Discard_Reason__c;
                journal.Journal_Status__c = 'In Process';   // New Journal (Reversed) is In Process
                journal.Type__c = objTran.Type__c;
                insert journal;
                
                objTran.Reversed_Journal__c = journal.id;
                update objTran;
                
                // Create Line Item for Reversal Journal
                List<Line_Item__c> lstTLI = new  List<Line_Item__c>(); 
                for (Transaction_Line_Item__c objL:objTran.Transaction_Line_Items__r){                      
                    Line_Item__c li = new Line_Item__c();
                    li.Journal__c = journal.id;
                    li.Account__c = objL.Account__c; 
                    li.Amount__c = -1*(objL.Amount__c); 
                    li.Currency__c  = objL.Currency__c; 
                    li.Credit__c = objL.Credit__c; 
                    li.Dimension_1__c  = objL.Dimension__c; 
                    li.General_Ledger_Account__c = objL.General_Ledger_Account__c; 
                    li.Line_Description__c = objL.Line_Description__c; 
                    li.Line_Type__c  = objL.Line_Type__c; 
                    li.Reference__c = objL.Reference__c; 
                    li.Transaction_Date__c  = objL.Transaction_Date__c; 
                    lstTLI.add(li);
                }
                if(lstTLI.size() > 0)  { 
                    insert lstTLI;    
                }
            }
            return strMSG;
        }
        catch(Exception e)
        {
            strMsg = ''+e.getMessage();
            return strMSG;
        }
    } 
}