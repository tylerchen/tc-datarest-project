;
var upperCaseByEvent = window['upperCaseByEvent'] || function(event){
    var val=$(event.target).val();
    if(val && val.length>0){
        $(event.target).val(val.toUpperCase());
    }
};
var capitalizeByEvent = window['capitalizeByEvent'] || function(event){
    var val=$(event.target).val();
    if(val && val.length>0){
        $(event.target).val(val.substring(0,1).toUpperCase()+val.substring(1));
    }
};
var uncapitalizeByEvent = window['uncapitalizeByEvent'] || function(event){
    var val=$(event.target).val();
    if(val && val.length>0){
        $(event.target).val(val.substring(0,1).toLowerCase()+val.substring(1));
    }
};

var jsonSchema = {
    name:{type: 'string', pattern: '^[a-zA-Z][a-zA-Z0-9_]*$', description: '字母、数字及下划线：^[a-zA-Z][a-zA-Z0-9_]*$', title: '模块名称', required: true},
    packageName:{type: 'string', pattern: '^[a-z][a-z0-9_\\.]*$', description: '小写字母、数字及._：^[a-z][a-z0-9_\\.]*$', title: '模块包名', required: true},
    alias:{type: 'string', title: '模块的中文名称'}
};

var formSchema = [
    {key: 'name'},
    {key: 'packageName'},
    {key: 'alias'},
    {type: 'submit', title: 'OK Go - This Too Shall Pass'}
];

var defaultValue={
    name:'Management',
    packageName:'management',
    alias:'管理'
};
