<div class="modal" id="projectModify">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">项目编辑</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="projectCode" class="col-sm-3 control-label">项目编号</label>
                        <div class="col-sm-8">
                            <input id="projectCode" class="form-control" value="" placeholder="输入项目编号"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="projectName" class="col-sm-3 control-label">项目名称</label>
                        <aside class="col-sm-8">
                            <input id="projectName" class="form-control" value="" placeholder="输入项目名称"/>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="rootPackage" class="col-sm-3 control-label">包根名称</label>
                        <aside class="col-sm-8">
                            <input id="rootPackage" class="form-control" value="" placeholder="输入包根名称"/>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="subClass" class="col-sm-3 control-label">分类文本</label>
                        <aside class="col-sm-8">
                            <input id="subClass" class="form-control" value="" placeholder="输入包分类文本"/>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="rootDir" class="col-sm-3 control-label">文件根目录</label>
                        <aside class="col-sm-8">
                            <input id="rootDir" class="form-control" value="" placeholder="输入文件根目录"/>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="connDriverName" class="col-sm-3 control-label">驱动名</label>
                        <aside class="col-sm-8">
                            <select id="connDriverName" class="select2 form-control">
                                <#if (map.DBDriversList?? && map.DBDriversList?size>0)>
                                    <#list map.DBDriversList as driver>
                                        <option value="${driver.key!''}">${driver.value!''}</option>
                                    </#list>
                                <#else>
                                    <option value=""></option>
                                </#if>
                            </select>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="connUrl" class="col-sm-3 control-label">连接URL</label>
                        <aside class="col-sm-8">
                            <input id="connUrl" class="form-control" value="" placeholder="输入连接URL"/>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="connUserName" class="col-sm-3 control-label">数据连接用户名</label>
                        <aside class="col-sm-8">
                            <input id="connUserName" class="form-control" value="" placeholder="输入数据连接用户名"/>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="connPassword" class="col-sm-3 control-label">数据连接密码</label>
                        <aside class="col-sm-8">
                            <input id="connPassword" class="form-control" value="" placeholder="输入数据连接密码"/>
                        </aside>
                    </div>
                    <div class="form-group">
                        <label for="connPassword" class="col-sm-3 control-label"></label>
                        <aside class="col-sm-8">
                            <button class="btn bg-maroon" data-toggle="button" onclick="onSaveProject();">保存项目</button>
                            <button class="btn bg-maroon" data-toggle="button" onclick="connTest();">连接测试</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </aside>
                    </div>
                </form>
            </div>
            <!--
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
            -->
        </div>
    </div>
</div>
<script>

    var onProjectChanged_fun;

    function collectData(){
        var data = {
            projectCode: $('#projectCode').val(),
            projectName: $('#projectName').val(),
            rootPackage: $('#rootPackage').val(),
            subClass: $('#subClass').val(),
            rootDir: $('#rootDir').val(),
            connDriverName: $('#connDriverName').val(),
            connUrl: $('#connUrl').val(),
            connUserName: $('#connUserName').val(),
            connPassword: $('#connPassword').val()};
        return data;
    }
    function onSaveProject() {
        var data = collectData();

        $.ajax({
            url: "/autoCode/onSaveProject",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (retData) {
                try {
                    if (retData) {
                        if (retData.code==0 && retData.projectMain) {
                            $('#projectCode').attr('disabled','disabled');
                            if (onProjectChanged_fun!=undefined && onProjectChanged_fun!=null &&
                                typeof onProjectChanged_fun === "function")
                                onProjectChanged_fun(retData.projectMain);
                            msgBox("项目保存成功！");
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
    function connTest() {
        var data = collectData();
        $.ajax({
            url: "/autoCode/connTest",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (retData) {
                try {
                    if (retData) {
                        if (retData.code==0) {
                            msgBox("测试数据连接成功！");
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
</script>