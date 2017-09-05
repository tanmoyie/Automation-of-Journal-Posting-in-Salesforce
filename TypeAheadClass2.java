/* This class is the custom controller for replacing lookup fields with autocomplete/typeahead
A controller for TypeAheadComponent2 component 
*/

public class TypeAheadClass2 {
    public String labelFieldVar{ get; set; }
    public String valueFieldVar{ get; set; }
    public String sObjVal{get;set;}
    public Integer randomJsIden{get;set;}
    public Object cacheField{get;private set;} 
    
    //  public SObject parentRecord { get; set; }
    //public String fieldName { get; set; }
    
    private Object targetFieldVar;
    //Constructor definition
    public TypeAheadClass2(){
        randomJsIden = getRandomNumber(1000000);
        sObjVal='Account';
        labelFieldVar='Name';
        valueFieldVar='Id';
    }
    
    /* Dummy setter Method */
    public void setCacheField(Object cacheField){}
    public void setTargetFieldVar(Object targetFieldVar){
        if(targetFieldVar != this.targetFieldVar){
            cacheField = getCacheFieldValue(targetFieldVar);
            this.targetFieldVar = targetFieldVar;
        }
    }
    // target field
    public Object getTargetFieldVar(){
        return targetFieldVar;
    }
    // collect field value for that object's field    
    private Object getCacheFieldValue(Object targetFieldVar){
        Object retVal = targetFieldVar;
        if(targetFieldVar!=null){
            for(sObject sObj : Database.query('SELECT '+valueFieldVar+','+labelFieldVar+' FROM '+sObjVal+' WHERE '+valueFieldVar+' =:targetFieldVar')){
                retVal = sObj.get(labelFieldVar);
                break;
            }
        }
        return retVal;
    }
    // change the js function name if multiple components are used    ***/
    private Integer getRandomNumber(Integer size){
        Double d = Math.random() * size;
        return d.intValue();
    }
    // json object 
    @RemoteAction
    public static List<AutoCompleteData> getData(String sObjVal,String labelFieldVar,String valueFieldVar,String param){
        List<AutoCompleteData> AutoCompleteDatas = new List<AutoCompleteData>();
        param = String.escapeSingleQuotes(param);
        for( Sobject sObj : Database.query('SELECT '+valueFieldVar+','+labelFieldVar+' FROM '+sObjVal+' WHERE '+labelFieldVar+' LIKE \'%'+param+'%\'')){
            AutoCompleteDatas.add(new AutoCompleteData(sObj.get(valueFieldVar),sObj.get(labelFieldVar)));
        }
        return AutoCompleteDatas; // return the suggested values
    }
    public class AutoCompleteData{
        public String id;
        public String text;
        public AutoCompleteData(Object id, Object text){
            this.id = String.valueOf(id);
            this.text = String.valueOf(string.valueof(text).trim());
        }
    }
}