/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest.core;

import com.google.gson.GsonBuilder;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.ThreadLocalHelper;
import org.iff.netty.server.ProcessContext;
import org.iff.netty.server.handlers.ActionHandler;
import org.iff.netty.server.handlers.BaseActionHandler;

import java.util.Arrays;
import java.util.Map;

import static org.iff.datarest.core.model.CrudModel.*;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class GenerateCodeActionHandler extends BaseActionHandler {
    public static final String uriPrefix = "/gen";

    public boolean execute(ProcessContext ctx) throws Exception {
        byte[] content = ctx.getContent();
        String json = new String(content, "UTF-8");
        Map map = GsonHelper.toJsonMap(json);

        ProjectModel pm = new ProjectModel().setValues(
                ProjectModel.id, "1",
                ProjectModel.groupId, "org.iff.test",
                ProjectModel.artifactId, "test-project",
                ProjectModel.version, "1.0.0",
                ProjectModel.templateVersion, "1.0.0",
                ProjectModel.modules, Arrays.asList(new ModuleModel().setValues(
                        ModuleModel.id, "id",
                        ModuleModel.name, "core",
                        ModuleModel.packageName, "core",
                        ModuleModel.alias, "Core",
                        ModuleModel.businesses, Arrays.asList(new BusinessModel().setValues(
                                BusinessModel.id, "1",
                                BusinessModel.name, "TestModel",
                                BusinessModel.packageName, "test",
                                BusinessModel.alias, "Test",
                                BusinessModel.tableName, "TEST_MODEL",
                                BusinessModel.actions, Arrays.asList(new ActionModel().setValues(
                                        ActionModel.id, "1",
                                        ActionModel.actionName, "Add",
                                        ActionModel.eventName, "add",
                                        ActionModel.actionType, "add",
                                        ActionModel.shortCut, "A",
                                        ActionModel.actionIcon, "actionIcon"
                                )),
                                BusinessModel.functions, Arrays.asList(new FunctionModel().setValues(
                                        FunctionModel.id, "1",
                                        FunctionModel.name, "User",
                                        FunctionModel.fileName, "index",
                                        FunctionModel.alias, "alias",
                                        FunctionModel.type, "type",
                                        FunctionModel.fields, "fields"
                                ))
                        ))
                ))
        );

        System.out.println("==json==" + new GsonBuilder().setPrettyPrinting().create().toJson(map));
        return true;
    }

    public boolean done() {
        ThreadLocalHelper.remove();
        return super.done();
    }

    public boolean matchUri(String uri) {
        return uriPrefix.equals(uri) || (uri.startsWith(uriPrefix)
                && (uri.charAt(uriPrefix.length()) == '/' || uri.charAt(uriPrefix.length()) == '?'));
    }

    public int getOrder() {
        return 100;
    }

    public ActionHandler create() {
        return new GenerateCodeActionHandler();
    }

}
