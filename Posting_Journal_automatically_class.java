// This class is a custom controller for the visualforce page of Accounting Module - Journal new record creating page
// This controller will contain the behavior to create and store Journal & Journal Line Item in database
//   
//   All right reserved Tanmoy Das @Dec-2016

public class JournalNewClass22 {
    public Journal__c objJour {get;set;}
    //    public List<Line_Item__c> objLine {get;set;}    
    public boolean pb1Rendered{get;set;} // 
    public boolean pb2Rendered{get;set;} // 
    public boolean pb3Rendered{get;set;} // true -> show pb3
    public String selectedRT{get;set;} // selected value of selectList
    public Attachment attachment {get; set;}
    // Lookup aotocomplete -->
    public String Company {get; set;}
    public String BankAccount {get; set;}
    public String type {get; set;}
    
    
    public List<wrapperClass> listWrapper {get; set;}
    public class wrapperClass{
        public String GLA {get; set;}
        public String GLA2 {get; set;}
        public String Company {get; set;}
        public String BankAccount {get; set;}
        public String Dimension {get; set;}
        public String Account {get; set;}
        public String TaxCode {get; set;}    
        public String type {get; set;}    
        public String targetField {get; set;}
        public Line_Item__c objLine {get; set; }
        public wrapperClass(){
            GLA = '';
            GLA2 = '';
            Company = '';
            BankAccount = '';
            Dimension = '';
            Account = '';
            TaxCode = '';
            type= '';
            targetField = '';
            objLine = new Line_Item__c();
        }
    }   
    // offset GLA
    public List<wrapperClass2> listWrapper2 {get; set;}
    public class wrapperClass2{
        public String GLA2 {get; set;}
        public String TaxCode {get; set;}    
        public String Company {get; set;}
        public String BankAccount {get; set;}
        public String type {get; set;}
        
        public Line_Item__c objLine {get; set; }
        public wrapperClass2(){
            GLA2 = '';
            Company = '';
            BankAccount = '';
            TaxCode = '';
            type ='';
            objLine = new Line_Item__c();
        }
    }
    
    // Constructor
    public JournalNewClass22(){
        attachment = new Attachment();
        pb1Rendered = false;
        pb2Rendered = false;
        pb3Rendered = true;
        selectedRT = '';
        
        listWrapper = new List<wrapperClass>();
        listWrapper.add(new wrapperClass());
        listWrapper2 = new List<wrapperClass2>();
        listWrapper2.add(new wrapperClass2());
        
        //   objLine = new List<Line_Item__c>();        
        //   objLine2 = new List<Line_Item__c>();
        objJour = new Journal__c();
    }
    // Picklist value for Journal Type
    public void onChangeFnCall(){
        if(selectedRT=='Bank'){
            pb1Rendered = true;
            pb2Rendered = false;
            pb3Rendered = false;
            objJour.Type__c = 'Bank';
            
        }
        else if(selectedRT=='Manual'){
            pb1Rendered = false;
            pb2Rendered = true;
            pb3Rendered = false;
            objJour.Type__c = 'Manual';
        }
        else if(selectedRT=='Sales'){
            pb1Rendered = false;
            pb2Rendered = false;
            pb3Rendered = true;
            objJour.Type__c = 'Sales';
        }
        else{
            pb1Rendered = false;
            pb2Rendered = false;
            pb3Rendered = true;
            objJour.Type__c = 'Payable';
        }
    }
    // Save button to create the Journal & Line Item records
    public PageReference save_custom()    
    {
        List<Line_Item__c> objLine = new List<Line_Item__c>();
        
        if(objJour.Type__c == null )
        {
            // Show message when required fields are empty
            ApexPages.Message myMsg = new ApexPages.Message(ApexPages.Severity.ERROR,'Journal type is required.');
            ApexPages.addMessage(myMsg);
            return null;
        }
        if(Company != null && Company !='')
        {
            objJour.Company__c = Company; 
            objJour.Bank_Account__c= BankAccount; 
        }    
        else
        {
            ApexPages.Message myMsg = new ApexPages.Message(ApexPages.Severity.ERROR,'Company is required.');
            ApexPages.addMessage(myMsg);
            return null;
        }
        Insert objJour;
        
        for(wrapperClass L:listWrapper){
            Boolean doCreate = false;
            
            Line_Item__c obj = L.objLine;
            obj.Journal__c = objJour.id; 
            if(L.Account != null && L.Account.trim() != ''){
                obj.Account__c = L.Account;
                doCreate = true;
            }            
            
            if(L.Dimension != null && L.Dimension.trim() != ''){
                obj.Dimension_1__c = L.Dimension;
                doCreate = true;
            }
            if(L.GLA != null && L.GLA.trim() != ''){
                obj.General_Ledger_Account__c = L.GLA;
                doCreate = true;                
            }
            if(L.TaxCode != null && L.TaxCode.trim() != ''){
                obj.Tax_Code__c = L.TaxCode;
                doCreate = true;                
            }
            if(L.type != null && L.type.trim() != ''){
                obj.Line_Type__c= 'General Ledger Account';
                doCreate = true;
            }
            if(L.GLA2 != null && L.GLA2.trim() != ''){
                obj.General_Ledger_Account_Offset__c = L.GLA2;
                doCreate = true;                
            }
            if(doCreate  ){
                objLine.add(obj);
            }
        } 
        for(wrapperClass2 L2:listWrapper2){
            Boolean doCreate2 = false;
            Line_Item__c obj = L2.objLine;
            obj.Journal__c = objJour.id; 
            if(L2.GLA2 != null && L2.GLA2.trim() != ''){
                obj.General_Ledger_Account_Offset__c = L2.GLA2;
                doCreate2 = true;
            }
            if(L2.TaxCode != null && L2.TaxCode.trim() != ''){
                obj.Tax_Code__c = L2.TaxCode;
                doCreate2 = true;
            }
            if(L2.type != null && L2.type.trim() != ''){
                obj.Line_Type__c= 'Bank Account';
                doCreate2 = true;
            }
            if(doCreate2){
                objLine.add(obj);
            } 
        }
        if(objLine.size() > 0){
            Insert objLine; 
        }
        System.Debug('message');
        upload(objJour.Id);
        PageReference pr = new PageReference('/apex/Posting_Journal_automatically');  // redirect the page in the same VF page
        pr.setRedirect(true);
        return pr;
        /* }
catch(Exception obj)
{
ApexPages.Message myMsg = new ApexPages.Message(ApexPages.Severity.ERROR,obj.getMessage());
ApexPages.addMessage(myMsg);
return  null;
}*/
    } 
    // Cancel button to reset/ clear all the field value supplied to the input fields
    public PageReference cancel(){
        PageReference pr = new PageReference('/apex/Posting_Journal_automatically');  
        pr.setRedirect(true);     
        return pr;
    }
    // Add Child object record e.g. Line Item
    public void addLineItem(){
        if(objJour.Type__c == 'Bank'){
            wrapperClass2 objW = new wrapperClass2();
            listWrapper2.add(objW);
        }
        else{
            wrapperClass objW = new wrapperClass();
            listWrapper.add(objW);
        }
    }
    // Delete Child object record
    public void deleteLineItem(){
        listWrapper.remove(listWrapper.size()-1);
    }    
    // Upload Attachments
    public void upload(Id ParentId) {
        Attachment objA = new Attachment();
        objA.OwnerId = UserInfo.getUserId();
        objA.ParentId = ParentId; // the record the file is attached to
        objA.IsPrivate = true;
        objA.name = attachment.Name;
        objA.Body = attachment.Body;
        if(objA.Body != null){
            objA.Description = attachment.Description;
            insert objA;
            ApexPages.addMessage(new ApexPages.message(ApexPages.severity.INFO,'Attachment uploaded successfully'));
        }
    }
}