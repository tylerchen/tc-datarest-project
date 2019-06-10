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
    groupId:{type: 'string', pattern: '^[a-z][a-z0-9_\\.]*$', description: '小写字母、数字及._：^[a-z][a-z0-9_\\.]*$', title: 'Group Id', required: true},
    artifactId:{type: 'string', pattern: '^[a-zA-Z][a-zA-Z0-9_-\\.]*$', description: '字母、数字及._-：^[a-zA-Z][a-zA-Z0-9_-\\.]*$', title: 'Artifact Id', required: true},
    version:{type: 'string', pattern: '^[a-zA-Z][a-zA-Z0-9_-\\.]*$', description: '字母、数字及._-：^[a-zA-Z][a-zA-Z0-9_-\\.]*$', title: 'Version', required: true},
    templateVersion:{type: 'string', enum: ['1.0.0-element-2.0.0','1.0.0-layui-1.0.0'], title: '模板版本', required: true}
};

var formSchema = [
    {key: 'groupId'},
    {key: 'artifactId'},
    {key: 'version'},
    {key: 'templateVersion'},
    {type: 'submit', title: 'Save'}
];

var defaultValue={
    groupId: 'org.iff.test',
    artifactId: 'test-project',
    version:'1.0.0',
    version:'1.0.0-element-2.0.0'
};
