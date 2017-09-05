<!--   
This TypeAheadComponent2 is the component to be used within the VF page with component tag to autosuggest the lookup field, 
TypeAheadComponent_Class is the controller for this component
-->

<apex:component controller="TypeAheadClass2" selfClosing="true">
    <!-- Attributes -->
    <apex:attribute name="SObject"      description="Object to query"        type="String" assignTo="{!sObjVal}" required="true" />
    <apex:attribute name="labelField"   description="API Name of Field to display for label" type="String"   required="true" assignTo="{!labelFieldVar}"/>
    <apex:attribute name="valueField"   description="value of targetField" type="String" required="true" assignTo="{!valueFieldVar}" />
    <apex:attribute name="targetField"  description="Field of current object that will hold the selection."        type="Object" assignTo="{!targetFieldVar}"/>
    <apex:attribute name="inputFieldId"     description="Id of the field where the value will copied field using js"        type="String" />
    <apex:attribute name="importJquery"        description="Assign false if you dont want to jquery files"        type="Boolean" default="true" />
    <apex:attribute name="syncManualEntry"        description="Allow manual entry of data from autocomplete component."        type="Boolean" default="true" />
    <apex:attribute name="allowClear" description="Set true to give user a option to clear existing value" type="Boolean" default="true"/>        
    <apex:attribute name="Style" description="style for the input component" type="String"/>
    <apex:attribute name="maximumSelectionSize" description="size" type="Integer" />
    <apex:attribute name="multiple" description="multiple" type="Boolean" />
    
    <!-- Javascript files-->
    <apex:outputPanel rendered="{!importJquery}">
        <apex:includeScript value="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"/>
    </apex:outputPanel>
    <apex:includeScript value="{!URLFOR($Resource.select2, 'select2-3.4.2/select2.js')}"/>
    <apex:stylesheet value="{!URLFOR($Resource.select2, 'select2-3.4.2/select2.css')}"/>
    <script>
    var v2{!randomJsIden}
    var prevVal{!randomJsIden};
    function typeahead{!randomJsIden}(){
        var v2=this;
        jQuery(function($){ //on document.ready
            v2.init($)
        });
    }
    typeahead{!randomJsIden}.prototype={
        init : function($){
            var $elem = $( ".auto{!randomJsIden}" ).select2({
                minimumInputLength: 0,
                placeholder: " ",
                allowClear : {!allowClear},
                query: function (query) {
                    queryData{!randomJsIden}(query);
                },
                createSearchChoice:function(term, data) {
                    if({!syncManualEntry} == true){
                        return {id:term, text:term};
                    }
                }
            });
            
            $elem.on("select2-selecting", function(e) {
                $('.hiddenField{!randomJsIden}').val(e.val);
            });
            $elem.on("select2-removed", function(e) {
                $('.hiddenField{!randomJsIden}').val('');
            });
            if('{!cacheField}' !=''){
                $elem.select2("data", {id: "{!targetFieldVar}", text: "{!cacheField}"})  
            }  
        },
        
        triggerSearch :function(val){
            if(prevVal{!randomJsIden} != val){
                $=jQuery;
                prevVal{!randomJsIden} = val;
                var select = $('input.auto{!randomJsIden}');          
                var search = $('.select2-input')
                select.select2('open');
                search.val(val);
                search.trigger("input");
            }
        }
    }
    
    /* populates values in the combobox **/    
    function queryData{!randomJsIden}(query){
        
        Visualforce.remoting.Manager.invokeAction(
            '{!$RemoteAction.TypeAheadClass2.getData}','{!sObjVal}','{!labelFieldVar}','{!valueFieldVar}',query.term,
            function(result, event){
                //if success
                if(event.status){ 
                    var data = {results: []}
                    data.results = result;                            
                    query.callback( data);                           
                }
                else{
                    alert('Invalid Field/Object API Name : '+event.message);
                }
            }, 
            {escape: true}
        );
    }  
    </script>
    
    <apex:inputText style="{!Style}" styleClass="auto{!randomJsIden}" value="{!cacheField}" />
    <apex:outputPanel id="hiddenPanel">
        <apex:inputText value="{!targetField}" id="hiddenField"   styleClass="hiddenField{!randomJsIden}" style="display:none" />
    </apex:outputPanel>
    <script>v2{!randomJsIden} = new typeahead{!randomJsIden}({});</script>
    
    </apex:component>