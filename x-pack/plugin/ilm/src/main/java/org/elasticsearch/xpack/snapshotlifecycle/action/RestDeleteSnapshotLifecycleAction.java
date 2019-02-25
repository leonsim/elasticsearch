/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */

package org.elasticsearch.xpack.snapshotlifecycle.action;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

public class RestDeleteSnapshotLifecycleAction extends BaseRestHandler {

    public RestDeleteSnapshotLifecycleAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(RestRequest.Method.DELETE, "/_ilm/snapshot/{name}", this);
    }

    @Override
    public String getName() {
        return "ilm_delete_snapshot_lifecycle";
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) {
        String lifecycleId = request.param("name");
        DeleteSnapshotLifecycleAction.Request req = new DeleteSnapshotLifecycleAction.Request(lifecycleId);
        req.timeout(request.paramAsTime("timeout", req.timeout()));
        req.masterNodeTimeout(request.paramAsTime("master_timeout", req.masterNodeTimeout()));

        return channel -> client.execute(DeleteSnapshotLifecycleAction.INSTANCE, req, new RestToXContentListener<>(channel));
    }
}
