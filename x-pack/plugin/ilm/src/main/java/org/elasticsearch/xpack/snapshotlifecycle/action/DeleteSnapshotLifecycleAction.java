/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */

package org.elasticsearch.xpack.snapshotlifecycle.action;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContentObject;

import java.io.IOException;
import java.util.Objects;

public class DeleteSnapshotLifecycleAction extends Action<DeleteSnapshotLifecycleAction.Response> {
    public static final DeleteSnapshotLifecycleAction INSTANCE = new DeleteSnapshotLifecycleAction();
    public static final String NAME = "cluster:admin/ilm/snapshot/delete";

    protected DeleteSnapshotLifecycleAction() {
        super(NAME);
    }

    @Override
    public DeleteSnapshotLifecycleAction.Response newResponse() {
        return new Response();
    }

    public static class Request extends AcknowledgedRequest<DeleteSnapshotLifecycleAction.Request> {

        private String lifecycleId;

        Request(String lifecycleId) {
            this.lifecycleId = Objects.requireNonNull(lifecycleId, "id may not be null");
        }

        Request() {
        }

        public String getLifecycleId() {
            return this.lifecycleId;
        }

        @Override
        public ActionRequestValidationException validate() {
            return null;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            lifecycleId = in.readString();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeString(lifecycleId);
        }

        @Override
        public int hashCode() {
            return lifecycleId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Request other = (Request) obj;
            return Objects.equals(lifecycleId, other.lifecycleId);
        }
    }

    public static class Response extends AcknowledgedResponse implements ToXContentObject {

        public Response() {
        }

        public Response(boolean acknowledged) {
            super(acknowledged);
        }
    }
}
