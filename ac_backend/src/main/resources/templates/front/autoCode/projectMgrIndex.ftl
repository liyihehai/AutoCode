<html>
<head>
    <#include "../taglib.ftl">
</head>
<body>
<div class="container">
    <section class="row">
        <form class="form-inline">
            <label class="control-label">项目名称：</label>
            <select id="defaultModelName" class="select2 form-control" style="width: 200px"
                    onchange="onProjectSelectChange();">
                <#if (map.projectList?? && map.projectList?size>0)>
                    <#list map.projectList as project>
                        <option value="${project.projectCode!''}">${project.projectName!''}</option>
                    </#list>
                </#if>
            </select>
            <button class="btn bg-blue"
                    onclick="addProject()" type="button">新增项目
            </button>
            <button class="btn bg-maroon"
                    onclick="modifyProject()" type="button">更改项目
            </button>
            <button class="btn bg-maroon"
                    onclick="deleteProject()" type="button">删除项目
            </button>
        </form>
        <hr>
    </section>
    <section class="row">
        <aside class="col-md-6">
            <form class="form-horizontal">
                <div class="form-group form-inline">
                    <label class="control-label">数据表</label>
                    <button class="btn bg-maroon" onclick="makeProjectCode()" type="button">生成代码文件</button>
                    <label for="projectSubClass" class="control-label">项目分类</label>
                    <select id="projectSubClass" class="select2 form-control" style="width: 200px">
                        <#if (map.scList?? && map.scList?size>0)>
                            <#list map.scList as sc>
                                <option value="${sc!''}">${sc!''}</option>
                            </#list>
                        <#else>
                            <option value=""></option>
                        </#if>
                    </select>
                </div>
                <div class="row pre-scrollable" style="width: 100%;height: 400px;border:1px solid #AAAAAA">
                    <ul id="tableNameList" class="list-group" rows="25">
                        <#if (map.tableList?? && map.tableList?size>0)>
                        <#list map.tableList as table>
                            <div class="checkbox"><label><input type="checkbox" class="tableNameItem" id="${table!''}">${table!''}</label></div>
                        </#list>
                        </#if>
                    </ul>
                </div>
            </form>
        </aside>
        <aside class="col-md-6">
            <form class="form-horizontal">
                <div class="form-group" role="group">
                    <label for="clearInfo" class="control-label">代码生成信息</label>
                    <button id="clearInfo" class="btn bg-maroon" onclick="onClearInfo();" type="button">清空消息</button>
                </div>
                <div class="form-group" role="group">
                    <textarea id="makeInfo" rows="20" style="width: 100%">空</textarea>
                </div>
            </form>
        </aside>
    </section>
</div>
<!-- 弹出窗 项目新增，更改-->
<#include "./projectModify.ftl">
<!-- 弹出窗结束 -->
<script>
    var modProject = null;
    //新增项目,弹出模态编界面
    function addProject() {
        modProject = null;
        $('#projectModify').modal({backdrop: 'static', keyboard: false});
    }
    function setSubClass(subclass){
        $('#projectSubClass').html("");
        if (subclass!=undefined && subclass!=null){
            var scList=subclass.split(',');
            if (scList!=null && scList.length>0){
                var html = "";
                for(var i=0;i<scList.length;i++){
                    var sc = scList[i];
                    html += '<option value="'+sc+'">'+sc+'</option>';
                }
                $('#projectSubClass').html(html);
            }
        }
    }
    //项目更改
    function modifyProject(){
        var projectCode=$('#defaultModelName').val();
        if (projectCode==undefined || projectCode==null || projectCode=='')
            return;
        $.ajax({
            url: "/autoCode/queryProjectInfo",
            type: "POST",
            async: false,
            data: { projectCode : projectCode},
            success: function (retData) {
                try {
                    if (retData) {
                        if (retData.code==0 && retData.projectMain) {
                            modProject = retData.projectMain;
                            $('#projectModify').modal({backdrop: 'static', keyboard: false});
                        }
                        else
                            msgBox(retData.msg);
                    } else {
                        msgBox("网络错误···");
                    }
                } catch (err) {
                    msgBox(err);
                }
            },
            error: function () {
                msgBox("网络错误···");
            }
        });
    }
    function onProjectDeleted(projectCode) {
        var findStr="#defaultModelName option[value='"+ projectCode +"']";
        $(findStr).remove();
        onProjectSelectChange();
    }
    //删除选中的项目
    function deleteProject(){
        var v = $("#defaultModelName").val();
        if (v==undefined || v==null || v<=0)
            return;
        $.ajax({
            url: "/autoCode/deleteProject",
            type: "POST",
            data: {projectCode: v},
            success: function (retData) {
                try {
                    if (retData) {
                        if (retData.code==0 && retData.projectMain) {
                            onProjectDeleted(retData.projectMain.projectCode);
                        }
                        else
                        {   msgBox(retData.msg);}
                    } else {    msgBox("网络错误···");}
                } catch (err) {msgBox(err);}
            },
            error: function () {    msgBox("网络错误···");}
        });
    }

    function getSelTables(classname){
        var sel = "input[class='"+classname+"']:checked";
        var count = 0;
        var tables = "";
        $(sel).each(function(){
            if (count>0)
                tables += ',';
            tables+=$(this).attr('id');
            count++;
        });
        return tables;
    }

    function makeProjectCode(){
        var v = $("#defaultModelName").val();
        var tables=getSelTables("tableNameItem");
        if (v==undefined || v==null || v<=0 || tables=='')
            return;

        var data = {
            projectCode: $('#defaultModelName').val(),
            subClass:$('#projectSubClass').val(),
            tables:tables};

        $.ajax({
            url: "/autoCode/makeAutoCode",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (retData) {
                try {
                    if (retData) {
                        if (retData.code==0) {
                            if (retData.makeInfoList){
                                var html = "";
                                for(var i=0;i<retData.makeInfoList.length;i++){
                                    var mi = retData.makeInfoList[i];
                                    html += mi+'&#10;';
                                }
                                $('#makeInfo').html(html);
                            }
                        }
                        else
                        {   msgBox(retData.msg);}
                    } else {
                        msgBox("网络错误···");
                    }
                } catch (err) {
                    msgBox(err);
                }
            },
            error: function () {
                msgBox("网络错误···");
            }
        });
    }
    //模态界面退出时触发
    $("#projectModify").on('hidden.bs.modal',function(){
    //    $( '#projectModify' ).off().on( 'hide', 'hide.bs.modal');
    })
    //模态界面显示时触发
    $("#projectModify").on('show.bs.modal',function(){
        if (modProject!=undefined && modProject!=null)
        {
            $('#projectCode').attr('disabled','disabled');
            $('#projectCode').val(modProject.projectCode);
            $('#projectName').val(modProject.projectName);
            $('#rootPackage').val(modProject.rootPackage);
            $('#subClass').val(modProject.subClass);
            $('#rootDir').val(modProject.rootDir);
            $('#connDriverName').val(modProject.connDriverName);
            $('#connUrl').val(modProject.connUrl);
            $('#connUserName').val(modProject.connUserName);
            $('#connPassword').val(modProject.connPassword);
        }
        else{
            $('#projectCode').removeAttr("disabled");
            $('#projectCode').val('');
            $('#projectName').val('');
            $('#rootPackage').val('');
            $('#subClass').val('');
            $('#rootDir').val('');
            $('#connDriverName').val('');
            $('#connUrl').val('');
            $('#connUserName').val('');
            $('#connPassword').val('');
        }
    })

    //当项目成功保存时触发
    function onProjectChanged(project){
        if (modProject==null){
            //如果是新增保存成功
            var html='<option value="'+project.projectCode+'">'+project.projectName+'</option>';
            html=$("#defaultModelName").html()+html;
            $("#defaultModelName").html(html);
            $("#defaultModelName").val(project.projectCode);
            onProjectSelectChange();
        }else{
            //如果是更改项目
            var findStr="#defaultModelName option[value='"+ project.projectCode +"']";
            $(findStr).html(project.projectName);
            $("#defaultModelName").val(project.projectCode);
            onProjectSelectChange();
        }
    }

    function makeTableNameItem(name) {
        var item = '<div class="checkbox"><label><input type="checkbox" class="tableNameItem" id="' + name + '">' + name + '</label></div>';
        return item;
    }

    function onProjectSelectChange() {
        var data = {projectCode: $('#defaultModelName').val()};

        $.ajax({
            url: "/autoCode/queryDBSrcTableNames",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (retData) {
                try {
                    if (retData) {
                        if (retData.code==0 && retData.tableList) {
                            $('#tableNameList').html("");
                            var html = "";
                            for (var i = 0; i < retData.tableList.length; i++) {
                                var tablename = retData.tableList[i];
                                html += makeTableNameItem(tablename);
                            }
                            $('#tableNameList').html(html);
                        }
                        else
                        {
                            msgBox(retData.msg);
                            $('#tableNameList').html("");
                        }
                        if (retData.projectMain){
                            modProject=retData.projectMain;
                            setSubClass(modProject.subClass);
                        }
                    } else {
                        msgBox("网络错误···");
                    }
                } catch (err) {
                    msgBox(err);
                }
            },
            error: function () {
                msgBox("网络错误···");
            }
        });
    }

    function onClearInfo(){
        $('#makeInfo').html('');
    }

    $(function () {
        onProjectChanged_fun=onProjectChanged;
    })

</script>
</body>
</html>

